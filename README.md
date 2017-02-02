# resume_classifier
An NLP based document classifier java project given the class labels as folder names.
Hi,
This is a java eclipse project implementation zip of Document Classifier (Coded by me from scratch) which can can classify any type of text documents into the following classes:
1. Research and Development
2. Software Engineering
3. Design and Engineering
4. Analyst QA and Testing
5. Letters or Essays
6. Management
7. Spam or Advertisements.

All you have to do is delete the classified files folder and put the documents required to be classified into the test folder and run the main from eclipse. You can find the results in the classified files folder as well as on the console of Eclipse.

Few notes:
This software is distributed under Apache License. Please request me before redistributing it. I am not liable for any consequences of using this software. Use it by citing this repository along with my email achyutsarmaboggaram@gmail.com and full name Achyut Sarma Boggaram.
You are welcome to fork and send me pull requests.
There is a function (code) in main available to directly rip email attachments, filter text documents and convert them to UTF-8 from eclipse.
These classes can be whatever you want them to be
As of now the system is minimally trained with minimal data for demonstration purposes.
The system can be trained with as much data as possible (implementation is a scaled up one which can cater the real world needs)
To train the system with new data, one needs to delete trained parameters folder and add training documents into learn folder by labeling the classes as sub-folders in which the documents belong to. (See already existing files for examples)
Also this prototype can be easily ported to any number of classes and to any platforms like android and web easily.
This system can be easily converted as an applet.
Also if you want to automate the categories, k means clustering can be done on a huge document corpus before training the classifier.
Here Naive Bayes is used, which is a decent text classifier but any other better classifiers can be used.
Please let me know if you need anything else or if I can change anything for better.
