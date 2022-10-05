import React from "react";
import personSrc from "../img/person.png";
import { Link, useParams } from "react-router-dom";

function DisplayUsers({ users, preferences }) {
  const currentLoggedIn = JSON.parse(localStorage.getItem("id"));
  const { user, travel, travelName } = useParams();
  function CreateUser({ username, personId, spent }) {
    return (
      <Link
        to={`/${user}/${travel}/${travelName}/profile/${username}`}
        state={{ personId: personId }}
      >
        <div className="user">
          {preferences.displayIcon ? (
            <img className="user-icon" src={personSrc} alt="profile" />
          ) : null}
          <br />
          <h4
            className="caption"
            style={{ color: currentLoggedIn === user.name ? "blue" : "black" }}
          >
            {username}
          </h4>
          <br />
          {preferences.displayMoney ? (
            <h4 className="caption">
              {spent >= 0 ? `₩${Math.round(spent)}` : `₩${-Math.round(spent)}`}
            </h4>
          ) : null}
        </div>
      </Link>
    );
  }

  function CreateType({ type }) {
    return (
      <div className="home-type">
        <h4 className="type">{type}</h4>
        <div style={{ verticalAlign: "center", alignItems: "center" }}>
          {users
            .filter(
              (user) =>
                (user.role && type === "총무") ||
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
            ))}
        </div>
      </div>
    );
  }

  return (
    <div className="users">
      <CreateType users={users} type="총무" />
      <CreateType users={users} type="Receive" />
      <CreateType users={users} type="Send" />
    </div>
  );
}

export default DisplayUsers;
