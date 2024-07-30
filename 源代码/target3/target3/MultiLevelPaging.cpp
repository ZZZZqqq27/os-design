//
//  MultiLevelPaging.cpp
//  target3
//
//  Created by 张智清 on 2024/7/19.
//

#include "MultiLevelPaging.hpp"

#include <sstream>
using namespace std;
MultiLevelPaging::MultiLevelPaging() : topLevelIndex(maxBlocks) {
}

int MultiLevelPaging::writeData(const string &data, int dataIndex) {
    // 遍历当前级别的所有块
    for (int blockIndex = 0; blockIndex < maxBlocks; ++blockIndex) {
        // 获取当前块
        Block &block = getBlock(blockIndex);

        // 计算当前块剩余的可用空间
        int remainingSpace = blockSize - block.usedSize;

        // 如果已经写完所有数据，则返回当前数据索引
        if (dataIndex >= static_cast<int>(data.size())) {
            return dataIndex;
        }

        // 计算当前可以写入的字节数
        int byteToWrite = min(remainingSpace, static_cast<int>(data.size()) - dataIndex);

        // 将数据复制到当前块的可用空间中
        copy(data.begin() + dataIndex, data.begin() + dataIndex + byteToWrite, block.data + block.usedSize);

        // 更新块的已用空间大小
        block.usedSize += byteToWrite;

        // 更新数据索引
        dataIndex += byteToWrite;
    }

    // 返回新的数据索引
    return dataIndex;
}

string MultiLevelPaging::readData() const {
    // 创建一个字符串输出流，用于存储读取的数据
    ostringstream oss;

    // 遍历当前级别的所有块
    for (const auto &block : blocks) {
        // 如果块中有已用数据空间，则将其内容写入输出流
        if (block.usedSize > 0) {
            oss.write(block.data, block.usedSize);
        }
    }

    // 返回读取到的数据字符串
    return oss.str();
}

Block &MultiLevelPaging::getBlock(int index) {
    // 如果索引超出当前块集合的大小，则调整块集合大小以包含该索引
    if (index >= blocks.size()) {
        blocks.resize(index + 1);
    }

    // 返回指定索引的块的引用
    return blocks[index];
}

void MultiLevelPaging::write(const string &data) {
    // 初始化数据索引为0
    int dataIndex = 0;

    // 遍历顶层索引的每一个级别
    for (int levelIndex = 0; levelIndex < topLevelIndex.size(); ++levelIndex) {
        // 如果所有数据已经写入，则跳出循环
        if (dataIndex >= static_cast<int>(data.size())) {
            break;
        }

        // 如果当前级别不存在子索引，则创建新的多级分页对象
        if (topLevelIndex[levelIndex] == nullptr) {
            topLevelIndex[levelIndex] = make_unique<MultiLevelPaging>();
        }

        // 将数据写入当前级别，并更新数据索引
        dataIndex = topLevelIndex[levelIndex]->writeData(data, dataIndex);

        // 如果还有剩余数据需要写入，则继续扩展多级索引
        while (dataIndex < static_cast<int>(data.size())) {
            // 创建新的多级分页对象
            auto newLevel = make_unique<MultiLevelPaging>();

            // 将剩余数据写入新的多级分页对象，并更新数据索引
            dataIndex = newLevel->writeData(data, dataIndex);

            // 将新的多级分页对象添加到顶层索引中
            topLevelIndex.push_back(move(newLevel));
        }
    }
}

string MultiLevelPaging::read() const {
    // 创建一个字符串输出流，用于存储读取的数据
    ostringstream oss;

    // 遍历顶层索引的每一个级别
    for (const auto &level : topLevelIndex) {
        // 检查当前级别是否存在
        if (level) {
            // 从当前级别读取数据并写入输出流
            oss << level->readData();
        }
    }

    // 返回读取到的数据字符串
    return oss.str();
}

