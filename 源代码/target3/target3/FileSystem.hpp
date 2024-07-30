//
//  FileSystem.hpp
//  target3
//
//  Created by 张智清 on 2024/7/19.
//


#ifndef FILESYSTEM_H
#define FILESYSTEM_H

#include <unordered_map>

#include "Directory.hpp"

// 文件权限常量
enum Permissions {
    READ = 1 << 0,     // 读权限
    WRITE = 1 << 1,    // 写权限
    EXECUTE = 1 << 2   // 执行权限
};

// 文件系统核心类，用于管理文件和目录
class FileSystem {
private:
    unique_ptr<Directory> root;                  // 根目录
    vector<bool> freeBlocks;                     // 块可用状态
    unordered_map<int, File*> fileDescriptorMap; // 文件描述符管理
    int nextFileDescriptor = 0;                  // 下一个可用的文件描述符

    Directory *getDirectory(const string &path);

    vector<string> splitPath(const string &path);

    int parsePermissions(const string &permissions);

    File *findFile(Directory *dir, const string &fileName);


    Directory *findDirectory(Directory *dir, const string &dirName);

public:
    // 构造函数
    FileSystem();

    // 创建文件
    // 参数:
    //   path - 目录路径
    //   fileName - 文件名
    //   permissions - 权限字符串
    void createFile(const string &path, const string &fileName, const string &permissions);

    // 打开文件
    // 参数:
    //   path - 目录路径
    //   fileName - 文件名
    // 返回:
    //   文件描述符
    int openFile(const string &path, const string &fileName);

   
    void closeFile(int fileDescriptor);

    void readFile(int fileDescriptor);

  
    void writeFile(int fileDescriptor, const string &data);


    void deleteFile(const string &path, const string &fileName);

    void createDirectory(const string &path, const string &dirName);


    void deleteDirectory(const string &path, const string &dirName);

 
    void listDirectory(const string &path);
};


#endif //FILESYSTEM_H
