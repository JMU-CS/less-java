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
