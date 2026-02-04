import logging
import os
from datetime import datetime

def setup_logging(log_file='squash_glados.log', file_log_level=logging.DEBUG, console_log_level=logging.INFO):
    logger = logging.getLogger('SquashGlados')
    
    # Check if handlers are already added to avoid duplicate logs
    if not logger.hasHandlers():
        logger.setLevel(logging.DEBUG)
        
        # File handler
        timestamp = datetime.now().strftime("_%Y%m%d_%H%M%S")
        year_month = datetime.now().strftime("%Y_%B")
        year_month_day = datetime.now().strftime("%Y_%m_%d")
        logfile_name, logfile_ext = log_file.rsplit('.', 1)
        logs_subdirs = f"logs/{year_month}/{year_month_day}"
        os.makedirs(logs_subdirs, exist_ok=True)
        log_file = f"{logs_subdirs}/{logfile_name}{timestamp}.{logfile_ext}"
        file_handler = logging.FileHandler(log_file)
        file_handler.setLevel(file_log_level)
        
        # Console handler
        console_handler = logging.StreamHandler()
        console_handler.setLevel(console_log_level)
        
        # Formatter
        formatter = logging.Formatter('%(asctime)s - %(name)s - %(module)s - %(funcName)s - %(levelname)s - %(message)s')
        file_handler.setFormatter(formatter)
        console_handler.setFormatter(formatter)
        
        # Add handlers to the logger
        logger.addHandler(file_handler)
        logger.addHandler(console_handler)
    
    return logger
