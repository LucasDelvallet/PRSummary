# PRSummary

PRSummary is a program that scan pull-request to make an analysis of the modifications of the files. It indicate some metrics about the addition/deletion/modification of some elements in the code. It only work with java files for the moment !

An exemple of message our program can make :
```
Number of java files modified/deleted/added : 5
Number of method added : 7
Number of method deleted : 1
Number of method modified : 6
Number of test added : 12
Number of test deleted : 0
```

## Installation
If you want the program to launch correctly, you have to create a file nammed .github holding these informations : 
```
login=YourLogin
password=YourPassword
```

The file must be located in the user folder, to know where the user folder is located, you can use this command : 

- For Unix/Linux : echo $HOME
- For Windows : echo %USERPROFILE%

## Execution

This is the command for the java program

```
java -jar PRSummary.jar RepoName Mode
```

The first mandatory argument **RepoName** correspond to the name of the repo you want to scan, for example : LucasDelvallet/PRSummary

The second mandatory argument **Mode** to the mode you want to launch PRSummary. There is two possibilities : 
- **Analysis** : This will launch a full analysis of all the PR of the repo.
- **Bot** : This will launch a bot that will scan the newly added PR of the repo and add a metric message.

A full example : 
```
java -jar PRSummary.jar LucasDelvallet/PRSummary Analysis
```

This will launch a full analysis of this repository.


## Thanks to

This program has been made possible by the use of two API :
- [The github-api by kohsuke] (https://github.com/kohsuke/github-api) that helped us to connect and interact with Github.
- [The Gumtree Spoon AST Diff](https://github.com/SpoonLabs/gumtree-spoon-ast-diff) That helped us to analyze the code and difference between two files

[Fine-grained and Accurate Source Code Differencing](https://hal.archives-ouvertes.fr/hal-01054552) (Jean-Rémy Falleri, Floréal Morandat, Xavier Blanc, Matias Martinez, Martin Monperrus), In Proceedings of the International Conference on Automated Software Engineering, 2014.
