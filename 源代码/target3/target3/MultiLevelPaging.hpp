//
//  MultiLevelPaging.hpp
//  target3
//
//  Created by 张智清 on 2024/7/19.
//


#ifndef MULTILEVELPAGING_H
#define MULTILEVELPAGING_H
#include <vector>

#include "Block.hpp"

using namespace std;

// 多级分页类，用于模拟多级分页结构
class MultiLevelPaging {
private:
    // 顶层索引，存储多级分页结构的指针
    vector<unique_ptr<MultiLevelPaging> > topLevelIndex;
    // 块集合，用于存储实际数据的块
    vector<Block> blocks;

    // 将数据写入当前级别的块
    // 参数:
    //   data - 要写入的数据
    //   dataIndex - 当前数据写入的起始索引
    // 返回:
    //   新的dataIndex，表示已经写入数据后的索引位置
    int writeData(const string &data, int dataIndex);

    // 从当前级别读取数据
    // 返回:
    //   读取到的字符串数据
    string readData() const;

    // 获取指定索引的块
    // 参数:
    //   index - 块的索引
    // 返回:
    //   对应索引的块的引用
    Block &getBlock(int index);

public:
    // 构造函数，初始化顶层索引
    MultiLevelPaging();

    // 写入数据到多级分页结构
    // 参数:
    //   data - 要写入的数据
    void write(const string &data);

    // 读取多级分页结构中的数据
    // 返回:
    //   读取到的字符串数据
    string read() const;
};


#endif //MULTILEVELPAGING_H
