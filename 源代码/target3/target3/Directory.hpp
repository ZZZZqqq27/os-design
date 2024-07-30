//
//  Directory.hpp
//  target3
//
//  Created by 张智清 on 2024/7/19.
//

#ifndef DIRECTORY_H
#define DIRECTORY_H

#include "File.hpp"

class Directory {
public:
    string name;
    vector<unique_ptr<File> > files;
    vector<unique_ptr<Directory> > directories;

    Directory(const string& name);
};



#endif //DIRECTORY_H
