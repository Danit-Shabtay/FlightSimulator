## Flight Simulator ✈️


### Background
Final project in Advanced software development course
as part of our B.Sc. Computer Science studies.
The project presents a visual analysis of flight control details.


The Flight simulator can connect to the FlightGear software according to TCP protocol,
Our simulator knows to tell us where anomalies in the values ​​of the interrogated interrogation were detected given a normal flight file and another interrogated flight file.
We have developed for the investigation of the anomalies anomalies algorithms.

The app does not know the anomaly algorithms in advance but loads them as a plug-in,
And so the user can build his own anomaly algorithms and add them himself.

---

### MileStones
<img src="img\img01.png" width=555>

---

### MVVM
There is a clear separation between the three parts: view, viewmodel and model.
This is a technique that uses the observer's design template, where the view is the viewer of the view model and the viewmodel is the viewer of the model,
In addition, there is a dataBaiding axis between the view and viewModel.
The viewmodel uses the model to perform the required calculations and immediately after performing all the calculations a notification is sent from the model to the viewmodel and then the viewmodel updates its datammber and immediately after the update you can see the change in view and this is due to the existing databiding. And finally the view shows it to the user.
<img src="img\img02.png" width=555>

---

### Demo screen
In the 'Load' button we load the CSV file which gives commands to the plane,
And after clicking play the plane starts taking off,
You can run forward and not wait for takeoff a lot of time.

<img src="img\img03.png" width=555>

---

