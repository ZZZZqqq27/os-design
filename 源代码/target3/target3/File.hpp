//
//  File.hpp
//  target3
//
//  Created by 张智清 on 2024/7/19.
//

#ifndef File_hpp
#define File_hpp

#include "MultiLevelPaging.hpp"
#include <string>

using namespace std;

class File {
public:
    string name; // 文件名
    string permissions; // 权限
    int size; // 文件大小
    unique_ptr<MultiLevelPaging> mlp; // 文件内容的多级分页对象

    // 构造函数，初始化文件名和权限
    File(const string& name, const string& permissions);
};




#endif //FILE_H
