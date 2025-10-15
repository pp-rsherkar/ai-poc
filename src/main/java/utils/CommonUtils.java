package utils;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CommonUtils {
    public static int startDay = 0;
    public static int endDay = 0;

    public static String timeStampCalculation(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
    }

    public static String generateRandomString(){
        return UUID.randomUUID().toString().substring(0, 10);
    }

    public static String randomFourDigitNumber() {
        int number = (int)(Math.random() * 10000); // generates 0 to 9999
        return String.format("%04d", number);      // pads with leading zeros
    }

    public static String generateRandomNumber() {
        Random random = new Random();
        StringBuilder npi = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            npi.append(random.nextInt(10));
        }
        return npi.toString();
    }

    public static List<String> normalize(List<String> list) {
        return list.stream()
                .map(s -> s.replaceAll("\\s+", " ").trim()) // replaces multiple spaces and trims
                .collect(Collectors.toList());
    }

    public static List<String> normalizeObjectList(List<Object> list) {
        return list.stream()
                .map(Object::toString)
                .map(s -> s.replaceAll("\\s+", " ").trim())
                .collect(Collectors.toList());
    }

    public static List<String> parseCommaSeparatedString(String input) {
        if (input == null || input.isBlank()) {
            return List.of();
        }
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(opt -> !opt.isEmpty())
                .collect(Collectors.toList());
    }

    public static Map<String, List<String>> processDataTable(Map<String, String> map) {
        Map<String, List<String>> result = new LinkedHashMap<>();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey().trim();
            String keyValue = entry.getValue().trim();
            List<String> values = keyValue.contains(",")
                    ? Arrays.stream(keyValue.split(",")).map(String::trim).collect(Collectors.toList())
                    : Collections.singletonList(keyValue);
            result.put(key, values);
        }
        return result;
    }

    public static void selectAndClickElement(Locator locator, List<String> values){
        for (int i = 0; i < locator.count(); i++) {
            String text = locator.nth(i).innerText().trim();
            for (String value : values) {
                if (text.equalsIgnoreCase(value.trim())){
                    locator.nth(i).click();
                    break;
                }
            }
        }
    }

    public static String readJsonTestDataFile(String jsonFileName) throws IOException {
        InputStream is = CommonUtils.class.getClassLoader().getResourceAsStream(jsonFileName);
        if (is == null) {
            throw new IllegalArgumentException("File not found in resources folder: " + jsonFileName);
        }
        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }

    public static List<String> convertStringToList(String stringData){
        return Arrays.stream(stringData.split(","))
                .map(String::trim)
                .toList();
    }

    public static void uploadFile(Locator fileInput, String fileName) {
        Path filePath = Paths.get("src/main/resources/uploadfiles/" + fileName).toAbsolutePath();
        fileInput.setInputFiles(filePath);
    }

    public static void uploadFileThroughSystemDialog(Page page, String fileName) {
        Locator fileInput = page.locator("input[type='file']").first();
        fileInput.setInputFiles(Paths.get("src/main/resources/uploadfiles/" + fileName));
        ElementHandle fileInputHandle = fileInput.elementHandle();
        page.evaluate("element => element.dispatchEvent(new Event('change', { bubbles: true }))", fileInputHandle);
    }

    public static void uploadFile(Page page, int inputIndex, String locatorValue, String fileName) {
        Path basePath = Paths.get("src/main/resources", fileName);
        if (!Files.exists(basePath)) {
            basePath = Paths.get("src/main/resources/uploadfiles", fileName);
        }
        Locator fileInputs = page.locator("input[type='file']");
        int fileInputCount = fileInputs.count();
        Locator targetInput = null;
        if (fileInputCount == 1) {
            targetInput = fileInputs.first();
        } else if (inputIndex < fileInputCount) {
            targetInput = fileInputs.nth(inputIndex);
        }
        ElementHandle fileInputHandle = targetInput.elementHandle();
        targetInput.setInputFiles(basePath);
        page.evaluate("element => element.dispatchEvent(new Event('change', { bubbles: true }))", fileInputHandle);
        if (locatorValue.contains("%s")) {
            page.waitForSelector(String.format(locatorValue, fileName),
                    new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        } else {
            page.waitForSelector(locatorValue,
                    new Page.WaitForSelectorOptions().setState(WaitForSelectorState.VISIBLE));
        }
    }

    public static boolean isDownloadedFileAvailable(String fileName, String extension) {
        File downloadDir = new File(System.getProperty("user.home") + "/Downloads");
        File latest = Arrays.stream(Objects.requireNonNull(
                                downloadDir.listFiles((dir, name) -> name.matches(fileName + "( \\(\\d+\\))?\\." + extension))))
                .max(Comparator.comparingLong(File::lastModified))
                .orElse(null);

        return latest != null;
    }

    public static void hoverAndClick(Page page, BoundingBox box, Locator tooltipLocator) {
        int maxValue = -1;
        double clickX = -1;
        double clickY = -1;
        long startTime = System.currentTimeMillis();
        long maxTime = 60000;

        int step = box.width > 500 ? 10 : 5;

        for (int x = 0; x < box.width; x += step) {
            for (int y = 0; y < box.height; y += step) {
                if (System.currentTimeMillis() - startTime > maxTime) break;
                double absX = box.x + x;
                double absY = box.y + y;
                page.mouse().move(absX, absY);
                if (tooltipLocator.count() > 0 && tooltipLocator.first().isVisible()) {
                    String tooltipText = tooltipLocator.first().innerText().trim();
                    try {
                        int value = Integer.parseInt(tooltipText.replace(",", ""));
                        if (value > maxValue) {
                            maxValue = value;
                            clickX = absX;
                            clickY = absY;
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        if (clickX >= 0 && clickY >= 0) {
            page.mouse().click(clickX, clickY);
        }
    }

    public static boolean scrollElementIntoView(Locator container, Locator targetList, int maxScrolls, int scrollStep, Page page) {
        container.evaluate("el => el.scrollTop = 0");

        for (int j = 0; j < maxScrolls; j++) {
            for (int i = 0; i < targetList.count(); i++) {
                BoundingBox targetBox = targetList.nth(i).boundingBox();
                BoundingBox containerBox = container.boundingBox();

                if (targetBox != null && containerBox != null &&
                        targetBox.y >= containerBox.y &&
                        targetBox.y <= (containerBox.y + containerBox.height)) {
                    return true;
                }else if(targetBox != null && containerBox != null &&
                        targetBox.y < containerBox.y + containerBox.height &&
                        targetBox.y + targetBox.height > containerBox.y){
                    return true;
                }else{
                    container.evaluate("el => el.scrollBy(0, " + scrollStep + ")");
                }
            }
            container.evaluate("el => el.scrollBy(0, " + scrollStep + ")");
        }

        return false;
    }

    public static Path downloadFileAndMoveToSystemFolder(Download download) throws IOException {
        Path tempDownloadedFile = download.path();
        String fileName = download.suggestedFilename();
        Path downloadsFolder = Paths.get(System.getProperty("user.home"), "Downloads");
        Path targetFile = downloadsFolder.resolve(fileName);
        Files.move(tempDownloadedFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
        return targetFile;
    }

    public static void generateScheduleDaysIfNeeded() {
        YearMonth currentMonth = YearMonth.now();
        int maxDay = currentMonth.lengthOfMonth();
        int today = LocalDate.now().getDayOfMonth();
        int attempts = 0;
        if (startDay != 0 && endDay != 0) {
            return;
        }
        do {
            if (today >= maxDay - 1) {
                startDay = maxDay - 1;
                endDay = maxDay;
            } else {
                startDay = ThreadLocalRandom.current().nextInt(today, maxDay);
                endDay = ThreadLocalRandom.current().nextInt(startDay + 1, maxDay + 1);
            }
            attempts++;
            if (attempts > 10) break;
        } while (endDay <= startDay);
    }
}
