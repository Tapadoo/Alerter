# Contributing to Alerter

### Issues
Make sure to search through any issues that have already been filed at https://github.com/Tapadoo/Alerter/issues before opening a new issue. 

When creating an issue, please include:

1. The version of the library you're using
2. The version of Android you're testing on
3. The device you're testing on
4. A code snippet of how you're using the library
5. A short description of the issue
6. Optional - a potential solution to the issue (Remember, PRs are always welcome!)

Also, please read this [article on how to write the perfect bug report](https://medium.com/pitch-perfect/how-to-write-the-perfect-bug-report-6430f5a45cd).

### Pull requests

We welcome, and encourage PRs!

If you would like to contribute to the code you can do so through GitHub by forking the repository and submitting a pull 
request into the develop branch. When submitting code, please make every effort to follow existing conventions and style in order to keep the code as readable as possible.

It's important to give motivations & use cases for new features, as we want to keep the library lean & focused.

### Checkstyles
Make sure to run a check `./gradlew :alerter:assemble check` on the code against the quality checkers that are used by the project and fix error that get raised. See `app/build/reports/` for generated report files.
