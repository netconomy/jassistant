#!/usr/bin/env python3
import os
from pathlib import Path

header = '''/**
*  Copyright 2018 The JASSISTANT Authors.
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
**/
'''

def update_header(fpath):
    output = []
    with fpath.open() as fp:
        in_old_header = False
        for (idx, line) in enumerate(fp):
            if idx == 0 and line.rstrip() == '/**':
                in_old_header = True
                continue
            if in_old_header and line.rstrip() == '**/':
                in_old_header = False
                continue
            if in_old_header:
                continue
            output.append(line.rstrip('\n'))
    fpath.write_text((header + '\n'.join(output)).rstrip() + '\n')


for (dirpath, _, filenames) in os.walk('.'):
    for filename in filenames:
        if filename.endswith('.java'):
            fpath = Path(dirpath) / filename
            update_header(fpath)
