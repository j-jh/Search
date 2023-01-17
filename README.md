Search Utility
==========================

Multi threaded search engine in Java to emulate Grep-like functionalities. Recursively searches through directories and subdirectories for text files, which are then parsed line by line into word stems with use of Regex pattern. Uses a work queue to build an inverted index that can be searched through with partial and exact search functionalities. The inverted index stores word count, word frequency, and file path location from the directories containing text files. Results are outputted in Json format for readability. 

By Jadon Huang
