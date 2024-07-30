//
//  File.cpp
//  target3
//
//  Created by 张智清 on 2024/7/19.
//

#include "File.hpp"

#include <string>


File::File(const string &name, const string &permissions) : name(name), permissions(permissions), size(0),
                                                            mlp(make_unique<MultiLevelPaging>()) {
}
