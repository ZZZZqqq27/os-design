//
//  Block.cpp
//  target3
//
//  Created by 张智清 on 2024/7/19.
//
#include "Block.hpp"
#include <algorithm>

using namespace std;

Block::Block() {
    data = new char[blockSize];
    fill(data, data + blockSize, '\0');
}
