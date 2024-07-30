//
//  main.cpp
//  target3
//
//  Created by 张智清 on 2024/7/19.
//






#include <iostream>
#include <vector>
#include <cstdlib>
#include <ctime>
#include <cmath>
#include <iomanip>
#include <thread>



#include "FileSystem.hpp"


using namespace std;

void printDivider(char fillChar = '-', int width = 150) {
    cout << setfill(fillChar) << setw(width) << fillChar << setfill(' ') << endl;
}


void userInterface(FileSystem &fs) {
    string command;
    while (true) {
        cout << endl << "请输入命令（输入 'help' 查看帮助， 'exit' 退出）： " << endl;
        getline(cin, command);

        if (command == "exit") {
            break;
        }

        istringstream iss(command);
        string cmd, arg1, arg2, arg3, arg4;
        iss >> cmd >> arg1 >> arg2 >> arg3 >> arg4;

        try {
            if (cmd == "create") {
                if (arg1 == "file") {
                    if (arg4.empty()) {
                        cout << "错误：缺少文件权限参数" << endl;
                        continue;
                    }
                    fs.createFile(arg2, arg3, arg4);
                } else if (arg1 == "dir") {
                    fs.createDirectory(arg2, arg3);
                } else {
                    cout << "无效的命令参数" << endl;
                }
            } else if (cmd == "open") {
                if (arg1 == "file") {
                    int fd = fs.openFile(arg2, arg3);
                    cout << "文件描述符：" << fd << endl;
                } else {
                    cout << "无效的命令参数" << endl;
                }
            } else if (cmd == "close") {
                int fd = stoi(arg1);
                fs.closeFile(fd);
            } else if (cmd == "read") {
                int fd = stoi(arg1);
                fs.readFile(fd);
            } else if (cmd == "write") {
                int fd = stoi(arg1);
                fs.writeFile(fd, arg2);
            } else if (cmd == "delete") {
                if (arg1 == "file") {
                    fs.deleteFile(arg2, arg3);
                } else if (arg1 == "dir") {
                    fs.deleteDirectory(arg2, arg3);
                } else {
                    cout << "无效的命令参数" << endl;
                }
            } else if (cmd == "list") {
                fs.listDirectory(arg1);
            } else if (cmd == "help") {
                printDivider();
                cout << left << setw(15) << "cmd" << setw(40) << "arg1" << setw(30) << "arg2" << setw(30) << "arg3" <<
                        setw(30) << "arg4" << endl;
                printDivider();
                cout << left << setw(15) << "create" << setw(40) << "file" << setw(30) << "<路径：path>" << setw(30) <<
                        "<文件名：fileName>" << setw(30) << "<访问权限：permissions>" << endl;
                cout << left << setw(15) << "create" << setw(40) << "dir" << setw(30) << "<路径：path>" << setw(30) <<
                        "<目录名：dirName>" << endl;
                cout << left << setw(15) << "open" << setw(40) << "file" << setw(30) << "<路径：path>" << setw(30) <<
                        "<文件名：fileName>" << endl;
                cout << left << setw(15) << "close" << setw(40) << "<文件描述符：fileDescriptor>" << endl;
                cout << left << setw(15) << "read" << setw(40) << "<文件描述符：fileDescriptor>" << endl;
                cout << left << setw(15) << "write" << setw(40) << "<文件描述符：fileDescriptor>" << setw(30) << setw(30) << "<数据：data>"
                        << endl;
                cout << left << setw(15) << "delete" << setw(40) << "file" << setw(30) << "<路径：path>" << setw(30) <<
                        "<文件名：fileName>" << endl;
                cout << left << setw(15) << "delete" << setw(40) << "dir" << setw(30) << "<路径：path>" << setw(30) <<
                        "<目录名：dirName>" << endl;
                cout << left << setw(15) << "list" << setw(40) << "<路径：path>" << endl;
                cout << left << setw(15) << "help" << endl;
                cout << left << setw(15) << "exit" << endl;
                printDivider();
            } else {
                cout << "无效命令" << endl;
            }
        } catch (const exception &e) {
            cout << "错误：" << e.what() << endl;
        }
    }
}

int main(int argc, const char * argv[]) {
    FileSystem fs;
       userInterface(fs);
    return 0;
}
