import os
import subprocess
import time
from .squash_api_glados import SquashAPI 

class UtilityMethods:
    def __init__(self, squash_server="prod", logger=None):
        self.squash_api = SquashAPI(squash_server, logger)

    def run_rebot_merge(self, all_output_paths):
        """ Run rebot --merge with output paths created from the robot commands 
            This creates one log file with the logs for all the tests combined 
        """
        rebot_commands = "python -m robot.rebot --merge --output output.xml "
        number_logs = 0
        for outp in all_output_paths:
            full_log_path = os.path.join(os.getcwd(), outp, "output.xml")
            print(f'full_log_path = {full_log_path}')
            if os.path.exists(full_log_path):
                output_dir_a,output_dir_b = outp.split("/", 1)
                rebot_commands += output_dir_b + "/" + "output.xml "
                number_logs += 1
        if number_logs == 0:
            print('There are no log files to merge')
            return
        print("rebot_commands = " + str(rebot_commands))
        os.chdir("logs") 
        print("CWD = " + str(os.getcwd()))
        time.sleep(1)
        rebot_result = subprocess.Popen(rebot_commands, shell=True).wait()
        print("rebot_result = " + str(rebot_result)) 


    def get_execution_case_custom_data(self, execution_id):
        data = self.squash_api.get_execution_case_custom_data(execution_id)
        return data

    def get_execution_case_name(self, execution_id):
        case_name = self.squash_api.get_execution_case_name(execution_id)
        return case_name

