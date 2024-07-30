//
//  FileSystem.cpp
//  target3
//
//  Created by 张智清 on 2024/7/19.
//

#include "FileSystem.hpp"
#include <iostream>

FileSystem::FileSystem() {
    // 初始化根目录为 "/"
    root = make_unique<Directory>("/");
    // 初始化块的可用状态，所有块均设置为可用
    freeBlocks.resize(maxBlocks, true);
}

// 获取目录对象
// 参数:
//   path - 目录的路径
// 返回:
//   指向目录的指针，如果目录不存在则返回 nullptr
Directory *FileSystem::getDirectory(const string &path) {
    // 检查路径是否为空
    if (path.empty()) {
        return nullptr;
    }

    // 获取根目录的指针
    Directory *currentDir = root.get();

    // 如果路径是根目录，直接返回根目录指针
    if (path == "/") {
        return currentDir;
    }

    // 将路径分割成各级目录名
    vector<string> dirs = splitPath(path);

    // 遍历各级目录名，逐级查找目录
    for (const string &dirName: dirs) {
        // 在当前目录的子目录中查找匹配的目录名
        auto it = find_if(currentDir->directories.begin(), currentDir->directories.end(), [&](const auto &subDir) {
            return subDir->name == dirName;
        });

        // 如果找到匹配的子目录，更新当前目录指针
        if (it != currentDir->directories.end()) {
            currentDir = it->get();
        } else {
            // 如果找不到匹配的子目录，返回 nullptr
            return nullptr;
        }
    }

    // 返回找到的目录指针
    return currentDir;
}

// 将路径字符串分割成各级目录名
// 参数:
//   path - 目录路径字符串
// 返回:
//   包含各级目录名的向量
vector<string> FileSystem::splitPath(const string &path) {
    vector<string> dirs; // 用于存储各级目录名
    size_t pos = 0; // 当前处理的位置
    size_t nextPos; // 下一个分隔符的位置

    // 循环查找路径中的每一个 '/' 分隔符
    while ((nextPos = path.find("/", pos)) != string::npos) {
        // 如果分隔符不在起始位置，则提取子串作为一个目录名
        if (nextPos != pos) {
            dirs.push_back(path.substr(pos, nextPos - pos));
        }
        // 更新当前位置，跳过分隔符
        pos = nextPos + 1;
    }

    // 处理最后一个目录名
    if (pos < path.length()) {
        dirs.push_back(path.substr(pos));
    }

    return dirs; // 返回包含各级目录名的向量
}

// 解析权限字符串，将其转换为权限标志的整数表示
// 参数:
//   permissions - 权限字符串，如 "r", "w", "rw", "rwx"
// 返回:
//   权限标志的整数表示，如果权限字符串无效则返回 -1
int FileSystem::parsePermissions(const string &permissions) {
    // 如果权限字符串是 "r"，返回读权限标志
    if (permissions == "r") {
        return Permissions::READ;
    }
    // 如果权限字符串是 "w"，返回写权限标志
    else if (permissions == "w") {
        return Permissions::WRITE;
    }
    // 如果权限字符串是 "rw"，返回读和写权限标志
    else if (permissions == "rw") {
        return Permissions::READ | Permissions::WRITE;
    }
    // 如果权限字符串是 "rwx"，返回读、写和执行权限标志
    else if (permissions == "rwx") {
        return Permissions::READ | Permissions::WRITE | Permissions::EXECUTE;
    }
    // 如果权限字符串无效，返回 -1
    else {
        return -1;
    }
}

// 在指定目录中查找文件
// 参数:
//   dir - 指向目录对象的指针
//   fileName - 要查找的文件名
// 返回:
//   指向文件对象的指针，如果文件不存在则返回 nullptr
File *FileSystem::findFile(Directory *dir, const string &fileName) {
    // 使用 find_if 函数在目录的文件列表中查找匹配的文件名
    auto it = find_if(dir->files.begin(), dir->files.end(), [&](const auto &file) {
        return file->name == fileName;
    });

    // 如果找到匹配的文件，返回指向文件对象的指针；否则返回 nullptr
    return it != dir->files.end() ? it->get() : nullptr;
}

// 在指定目录中查找子目录
// 参数:
//   dir - 指向目录对象的指针
//   dirName - 要查找的子目录名
// 返回:
//   指向子目录对象的指针，如果子目录不存在则返回 nullptr
Directory *FileSystem::findDirectory(Directory *dir, const string &dirName) {
    // 使用 find_if 函数在目录的子目录列表中查找匹配的子目录名
    auto it = find_if(dir->directories.begin(), dir->directories.end(), [&](const auto &subDir) {
        return subDir->name == dirName;
    });

    // 如果找到匹配的子目录，返回指向子目录对象的指针；否则返回 nullptr
    return it != dir->directories.end() ? it->get() : nullptr;
}

// 在指定目录中创建文件
// 参数:
//   path - 目录路径
//   fileName - 文件名
//   permissions - 文件权限字符串
// 抛出:
//   runtime_error 如果目录不存在或文件已存在或权限非法
void FileSystem::createFile(const string &path, const string &fileName, const string &permissions) {
    // 获取指定路径的目录指针
    Directory *dir = getDirectory(path);
    if (!dir) {
        throw runtime_error("目录不存在");
    }

    // 检查文件是否已存在
    if (findFile(dir, fileName)) {
        throw runtime_error("文件已存在");
    }

    // 创建新的文件对象
    auto newFile = make_unique<File>(fileName, permissions);

    // 解析权限字符串
    int perm = parsePermissions(permissions);
    if (perm == -1) {
        throw runtime_error("非法权限");
    }

    // 初始化文件大小为 0
    newFile->size = 0;

    // 将文件添加到目录的文件列表中
    dir->files.push_back(move(newFile));
    cout << "文件创建成功" << endl;
}

int FileSystem::openFile(const string &path, const string &fileName) {
    // 获取指定路径的目录指针
    Directory *dir = getDirectory(path);

    // 如果目录不存在，抛出异常
    if (!dir) {
        throw runtime_error("目录不存在");
    }

    // 在目录中查找指定文件
    File *file = findFile(dir, fileName);

    // 如果文件不存在，抛出异常
    if (!file) {
        throw runtime_error("文件不存在");
    }

    // 解析文件权限
    int perm = parsePermissions(file->permissions);

    // 检查文件是否可读
    if (!(perm & Permissions::READ)) {
        throw runtime_error("文件不可读");
    }

    // 生成新的文件描述符并将文件与之关联
    int fd = nextFileDescriptor++;
    fileDescriptorMap[fd] = file;

    // 输出文件打开成功信息
    cout << "打开文件：" << file->name << endl;

    // 返回文件描述符
    return fd;
}

void FileSystem::closeFile(int fileDescriptor) {
    // 查找文件描述符对应的文件
    auto it = fileDescriptorMap.find(fileDescriptor);

    // 如果找到了文件描述符对应的文件
    if (it != fileDescriptorMap.end()) {
        // 从文件描述符映射中移除该文件
        fileDescriptorMap.erase(it);

        // 输出关闭文件描述符的信息
        cout << "关闭文件描述符：" << fileDescriptor << endl;
    } else {
        // 如果文件描述符无效，则抛出异常
        throw runtime_error("无效的文件描述符");
    }
}

void FileSystem::readFile(int fileDescriptor) {
    // 查找文件描述符对应的文件
    auto it = fileDescriptorMap.find(fileDescriptor);

    // 如果找不到对应的文件描述符，则抛出异常
    if (it == fileDescriptorMap.end()) {
        throw runtime_error("无效的文件描述符");
    }

    // 获取文件对象指针
    File *file = it->second;

    // 检查文件的读权限
    int perm = parsePermissions(file->permissions);
    if (!(perm & Permissions::READ)) {
        throw runtime_error("文件没有读权限");
    }

    // 读取文件内容
    string content = file->mlp->read();

    // 输出读取的文件内容
    cout << "读取文件内容：" << content << endl;
}

void FileSystem::writeFile(int fileDescriptor, const string &data) {
    auto it = fileDescriptorMap.find(fileDescriptor);
    if (it == fileDescriptorMap.end()) {
        throw runtime_error("无效的文件描述符");
    }

    File *file = it->second;

    // 检查权限
    int perm = parsePermissions(file->permissions);
    if (!(perm & Permissions::WRITE)) {
        throw runtime_error("文件没有写权限");
    }

    // 写入文件
    file->mlp->write(data);
    file->size += data.size();
    cout << "写入文件成功" << endl;
}

void FileSystem::deleteFile(const string &path, const string &fileName) {
    // 获取指定路径的目录指针
    Directory *dir = getDirectory(path);
    if (!dir) {
        throw runtime_error("目录不存在");
    }

    // 在目录的文件列表中查找匹配的文件
    auto it = find_if(dir->files.begin(), dir->files.end(), [&](const auto &file) {
        return file->name == fileName;
    });

    // 如果文件不存在，抛出异常
    if (it == dir->files.end()) {
        throw runtime_error("文件不存在");
    }

    // 在文件描述符映射中查找并删除指向该文件的条目
    for (auto fdIt = fileDescriptorMap.begin(); fdIt != fileDescriptorMap.end();) {
        if (fdIt->second == it->get()) {
            fdIt = fileDescriptorMap.erase(fdIt);
        } else {
            ++fdIt;
        }
    }

    // 从目录的文件列表中移除文件，智能指针会自动释放内存
    dir->files.erase(it);

    cout << "删除文件成功" << endl;
}

void FileSystem::createDirectory(const string &path, const string &dirName) {
    // 获取指定路径的目录对象
    Directory *dir = getDirectory(path);

    // 如果目录不存在，则抛出异常
    if (!dir) {
        throw runtime_error("目录不存在");
    }

    // 检查指定目录下是否已存在同名目录，如果存在，则抛出异常
    if (findDirectory(dir, dirName)) {
        throw runtime_error("目录已存在");
    }

    // 创建新目录并添加到指定目录下
    auto newDir = make_unique<Directory>(dirName);
    dir->directories.push_back(move(newDir));

    // 输出创建成功信息
    cout << "目录创建成功" << endl;
}

void FileSystem::deleteDirectory(const string &path, const string &dirName) {
    // 获取指定路径的目录对象
    Directory *dir = getDirectory(path);

    // 如果目录不存在，则抛出异常
    if (!dir) {
        throw runtime_error("目录不存在");
    }

    // 查找指定目录下是否存在目标目录
    auto it = find_if(dir->directories.begin(), dir->directories.end(), [&](const auto &subDir) {
        return subDir->name == dirName;
    });

    // 如果目标目录不存在，则抛出异常
    if (it == dir->directories.end()) {
        throw runtime_error("目录不存在");
    }

    // 删除目标目录
    dir->directories.erase(it);

    // 输出删除成功信息
    cout << "删除目录成功" << endl;
}

void FileSystem::listDirectory(const string &path) {
    // 获取指定路径的目录对象
    Directory *dir = getDirectory(path);

    // 如果目录不存在，则抛出异常
    if (!dir) {
        throw runtime_error("目录不存在");
    }

    // 输出目录内容
    cout << "目录内容：" << endl;

    // 遍历目录中的文件，并输出文件信息
    for (const auto &file: dir->files) {
        cout << "文件： " << file->name << " （权限： " << file->permissions << "），大小： " << file->size << " 字节" << endl;
    }

    // 遍历目录中的子目录，并输出子目录名
    for (const auto &subDir: dir->directories) {
        cout << "目录： " << subDir->name << endl;
    }
}
