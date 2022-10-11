import React, {useState} from "react";
import personSrc from "../img/person.png";
import { Link, useParams } from "react-router-dom";

function DisplayUsers({ users, preferences }) {
  const currentLoggedIn = JSON.parse(localStorage.getItem("id"));
  const { user, travel, travelName } = useParams();
  function CreateUser({ username, personId, spent }) {
    return (
      <Link
        to={`/${user}/${travel}/${travelName}/profile/${personId}`}
      >
        <div className="user">
          {preferences.displayIcon ? (
            <img className="user-icon" src={personSrc} alt="profile" />
          ) : null}
          <br />
          <h4
            className="caption" id="caption-name"
            style={{ color: currentLoggedIn === user.name ? "blue" : "black" }}
          >
            {username}
          </h4>
          {preferences.displayMoney ? (
            <h4 className="caption" id="caption-spent">
              {spent >= 0 ? `₩${Math.round(spent)}` : `-₩${-Math.round(spent)}`}
            </h4>
          ) : null}
        </div>
      </Link>
    );
  }

  function CreateType({ type }) {
    const user = users
    .filter(
      (user) =>
        (user.role && type === "Manager") ||
        (!user.role && user.difference >= 0 && type === "Receive") ||
        (user.difference < 0 && type === "Send")
    )
    .map((user) => (
      <CreateUser
        username={user.name}
        spent={user.difference}
        personId={user.personId}
        key={user.personId}
      />
    ))
    let displayType = "block";

    function checkDisplayType(){
      displayType = Object.keys(user).length === 0 ? "none" : "block";
    }
    checkDisplayType();
    
    return (
      <div className="home-type">
        <h4 className="type" style={{display: displayType}}>{type}</h4>
        <div style={{ verticalAlign: "center", alignItems: "center" }}>
          {user}
        </div>
      </div>
    );
  }

  return (
    <div className="users">
      <CreateType users={users} type="Manager" />
      <CreateType users={users} type="Receive" />
      <CreateType users={users} type="Send" />
    </div>
  );
}

export default DisplayUsers;
