# rmit2025a-cosc2469-group-project

For problem description, see [Assessment Details](project.pdf).

---

## Contribution

| Student Name         | Student ID | Contribution Score |
|:---------------------|:-----------|:------------------:|
| Nguyen Phuong Anh    | S-4040506  |         7          |
| Tran Ngoc Hong Doanh | S-3927023  |         7          |
| Nguyen Minh Khai     | S-3995223  |         7          |
| Nguyen Gia Khang     | S-4034066  |         7          |
| Nguyen Duc Trung     | S-4014896  |         7          |

## Project Structure

```
.
├── output/
│   ├── difficult2_steps.csv
│   └── easiest1_steps.csv
│   └── intermediate_steps.csv
│   └── notfun_steps.csv
├── src/
│   ├── main/java/vn/rmit/cosc2469
│   │   ├── MainRunner.java
│   │   ├── RMIT_Sudoku_Solver.java
│   │   └── SolverLogger.java
│   │   └── Sudoku_Tabu_Search_Solver.java
│   │   └── SudokuSolverHelper.java
│   └── test/java/vn/rmit/cosc2469
│       └── Sudoku_Tabu_Search_SolverTest.java
├── test-data/
│   ├── difficult1.csv
│   ├── difficult2.csv
│   ├── easiest1.csv
│   ├── easiest2.csv
│   ├── intermediate.csv
│   └── notfun.csv
├── .gitignore
├── pom.xml
└── README.md
```

## Development Environment

### Global Requirements

| Requirement                        |   Version    |
|:-----------------------------------|:------------:|
| [Git](https://git-scm.com)         |    latest    |
| [Maven](https://maven.apache.org/) |    3.9.9     |
| [OpenJDK](https://openjdk.org/)    | 17 or higher |

### Set Up

#### VSCode Requirements

Extension requirements for building and running Java classes:

| Requirement                                                                                             | Version |
|:--------------------------------------------------------------------------------------------------------|:-------:|
| [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) | latest  |
| [Test Runner for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-test)    | latest  |

To properly setup Java and Python development environment for our project, refer to the following documentation:

- [Java in VSCode](https://code.visualstudio.com/docs/languages/java).
- [Java Testing in VSCode](https://code.visualstudio.com/docs/java/java-testing).

To run tests, you need to install JUnit Maven dependency:

```bash
$ mvn install
```

#### IntelliJ IDEA

To properly setup OpenJDK 17 to work with IntelliJ IDEA toolchain, refer to this
documentation: [IntelliJ IDEA - SDKs](https://www.jetbrains.com/help/idea/sdk.html#change-module-sdk).

To run tests, you need to install JUnit:

```bash
$ mvn install
```

## Build and Execution

- **Running the Full-Backtracking Solver:**
  Our Main and final solution using the full-backtracking algorithm can be executed by running the `main` method within
  the `vn.rmit.cosc2469.MainRunner` class.
    - To run the algorithm on other Sudoku puzzles, you can modify the `filePath` variable at line 5 within the `main`
      method of `MainRunner.java`.
    - Simply change the .csv name file from the available resources. For example, to run the algorithm on easiest1
      puzzle, modify from: `String filePath = "test-data/notfun.csv"` to `String filePath = "test-data/easiest1.csv"`
    - All available Sudoku puzzles are available for testing are stored in `test-data` package, taken from
      this [Link](https://sandiway.arizona.edu/sudoku/examples.html).
- **Running Tabu Search Tests:**
  All unit tests for the Tabu Search solver are located within the `vn.rmit.cosc2469.Sudoku_Tabu_Search_Solver`.
    - You can execute these test cases using your IDE's JUnit runner directly or via Maven using the `mvn test` command.
    - The results of these tests (pass/fail status and any output from the tests themselves) will be displayed within
      the terminal or your IDE's test runner window. These tests are designed to verify the correctness and
      functionality of the Tabu Search implementation, and they do not generate separate step-by-step output files.

## Video Demonstration

Video available on OneDrive: [Link](https://rmiteduau-my.sharepoint.com/:v:/g/personal/s4034066_rmit_edu_vn/ER8mHjfgOnJCl7E3ulLXpOkBNKMzyUsOmEVjp4-J8q07uA?nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJPbmVEcml2ZUZvckJ1c2luZXNzIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXciLCJyZWZlcnJhbFZpZXciOiJNeUZpbGVzTGlua0NvcHkifX0&e=R4amsd).
