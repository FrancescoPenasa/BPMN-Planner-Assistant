# BPMN Planner Assistant
This is the prototype developed for my bachelor's thesis in computer science at the university of Trento.
The branch included with the thesis is the files-v branch. Following some short instructions on the configuration required in order to make usable all the branches of the repository. The thesis can be found at the following link https://github.com/FrancescoPenasa/Bachelors-thesis

* import the code as java plug-in

* install emf dep in eclipse
https://www.vogella.com/tutorials/EclipseEMF/article.html 

* install bpmn dep in eclipse
https://www.eclipse.org/bpmn2-modeler/ 

* in manifest add dependencies
'''
org.eclipse.bpmn2.edit 
org.eclipse.bpmn2.editor
org.eclipse.bpmn2.modeler.core
org.eclipse.bpmn2.modeler.help
org.eclipse.bpmn2.modeler.ui
org.eclipse.core.runtime
'''

* tested in ubuntu and linux mint 64-bit with openjdk 8
