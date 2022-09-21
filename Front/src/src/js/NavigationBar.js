import React from "react";
import gearSrc from "../img/gearshape.png";
import bracketSrc from "../img/leftAngleBracket.jpeg";
import calendarSrc from "../img/calendar.png";
import { Link } from "react-router-dom";

function NavigationBar({ preferences, setPreferences }) {
  const [tabActive, setTabActive] = React.useState("none");
  const [loggedIn, setLoggedIn] = React.useState(
    JSON.parse(localStorage.getItem("id"))
  );

  const onLogOutClick = () => {
    localStorage.removeItem("id");
    setLoggedIn(null);
  };
  const onClickDisplayIcon = () => {
    let newPreference = preferences;
    newPreference.displayIcon = !preferences.displayIcon;
    setPreferences(newPreference);
    localStorage.setItem("preferences", JSON.stringify(preferences));
  };
  const onClickDisplayMoney = () => {
    let newPreference = preferences;
    newPreference.displayMoney = !preferences.displayMoney;
    setPreferences(newPreference);
    localStorage.setItem("preferences", JSON.stringify(preferences));
  };

  function PreferenceTab() {
    return (
      <div style={{ display: tabActive }}>
        <span onClick={onClickDisplayIcon}>
          Display Icon: {preferences.displayIcon ? "ON" : "OFF"}
        </span>
        <br />
        <span onClick={onClickDisplayMoney}>
          Display Money: {preferences.displayMoney ? "ON" : "OFF"}
        </span>
        <br />
      </div>
    );
  }

  const onClickPreference = () => {
    setTabActive(tabActive === "block" ? "none" : "block");
  };
  return (
    <div style={{ display: "flex" }}>
      <h4 style={{ color: "white" }}>{loggedIn}</h4>
      <Link to="/">
        <button id="logout" onClick={onLogOutClick}>
          Log Out
        </button>
      </Link>
      <img
        className="barIcon"
        src={gearSrc}
        onClick={onClickPreference}
        alt="gear"
      />
      <PreferenceTab />
      {/* <img className="barIcon" src={bracketSrc} alt="leftBracket" />
      <img className="barIconReversed" src={bracketSrc} alt="rightBracket" />
      <Link to="/calendar">
        <img className="barIcon" src={calendarSrc} alt="calendar" />
      </Link> */}
    </div>
  );
}

export default NavigationBar;
