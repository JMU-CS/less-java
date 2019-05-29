[![Build Status](https://travis-ci.org/JMU-CS/less-java.svg?branch=master)](https://travis-ci.org/JMU-CS/less-java)

# A language to introduce students to programming
Get gradle aliases from my .dotfiles repo: [Gradle Aliases](https://github.com/Zamua/.dotfiles/blob/master/gradle-aliases.sh)

###### *Note: Scripts reference `gradle` and thus won't work if you don't have gradle installed
1. How to build the compiler:
    * If you have Gradle installed:
        `gradle build`
    * If not:
        * Unix:    `./gradlew build`
        * Windows: `gradlew build`

2. How to compile Less-Java source:
    * `./compile.sh <file-name>`

3. How to run the most recently compiled file:
    * `./run.sh`

4. How to run the tests in the most recently compiled file:
    * `./test.sh`

5. How to run all sample files against previously-known output:
    * `./dotests.sh -r tests`

6. How to set expected output for a sample file:
    * `./dotests.sh -s <file-name>`

7. If you would like to use Eclipse, then run `gradle eclipse` to generate project files.

If you use this project in an academic project, please cite the following paper:

Paper: Zamua O. Nasrawt and Michael O. Lam. 2019. Less-Java, more learning: language design for introductory programming. J. Comput. Sci. Coll. 34, 3 (January 2019), 64-72. [ACM DL](https://dl.acm.org/citation.cfm?id=3306476)
