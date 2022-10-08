import React from "react";
import "./App.css";
import Home from "./Home/Home";
import Profile from "./js/Profile";
import LogIn from "./Authentication/LogIn";
import Register from "./Authentication/Register";
import Calendar from "./Home/Calendar";
import { CreateEvent } from "./Create/CreateEvent";
import { ChangeEvent } from "./js/ChangeEvent";
import CreateUser from "./Create/CreateUser";
import EventDescription from "./js/EventDescription";
import { Routes, Route } from "react-router-dom";
import SelectTravel from "./Travel/SelectTravel";

const App = () => {
  return (
    <div>
      <Routes>
        <Route path="/" element={<LogIn />} />
        <Route
          path="/:user/:travel/:travelName/profile/:username"
          element={<Profile />}
        />
        <Route path="/login" element={<LogIn />} />
        <Route path="/register" element={<Register />} />
        <Route path="/calendar" element={<Calendar />} />
        <Route
          path="/:user/:travel/:travelName/createEvent"
          element={<CreateEvent />}
        />
        <Route
          path="/:user/:travel/:travelName/changeEvent"
          element={<ChangeEvent />}
        />
        <Route
          path="/:user/:travel/:travelName/createUser"
          element={<CreateUser />}
        />
        <Route
          path="/:user/:travel/:travelName/:event"
          element={<EventDescription />}
        />
        <Route path="/selectTravel" element={<SelectTravel />} />
        <Route path="/:user/:travel/:travelName" element={<Home />} />
      </Routes>
    </div>
  );
};

export default App;
