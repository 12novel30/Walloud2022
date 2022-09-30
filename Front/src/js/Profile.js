import axios from "axios";
import React, { useState, useEffect } from "react";
import { Link, useLocation, useParams } from "react-router-dom";

const Profile = () => {
  const { user, travel, travelName } = useParams();
  const personid = useLocation().state.personid;

  const [profile, setProfile] = useState({});
  const [person_in_List, seteventList] = useState([]);

  useEffect(() => {
    getProfile();
  }, []);

  const getProfile = async () => {
    await axios
      .get(`/api/${user}/${travel}/${personid}/personDetail`)
      .then((res) => {
        console.log(res.data);
        setProfile(res.data);
        console.log(res.data.eventList);
        // setList(res.data.eventList);
        seteventList(res.data.eventList);
        console.log(person_in_List);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const displayEvents = () => {
    const events = profile.eventList;
    console.log("eventList : ", events);

    return (
      <div style={{ display: "flex" }}>
        {events.map((event, index) => (
          <div>
            <Link
              to={`/${user}/${travel}/${travelName}/${event}`}
              state={{ event: event }}
            >
              <h3 className="link-text" key={index}>
                {event}
              </h3>
            </Link>
          </div>
        ))}
      </div>
    );
  };

  return (
    <div>
      <Link to={`/${user}/${travel}/${travelName}`}>
        <h1 className="home">{travelName}</h1>
      </Link>
      <h2>{profile.userName}</h2>
      <h3>Account : {profile.userAccount}</h3>
      {profile.travelRole ? <h3>Manager</h3> : <></>}
      <h3 style={{ color: profile.difference > 0 ? "red" : "blue" }}>
        Spent: {profile.difference}₩
      </h3>
      <h3 id="headers">Participated Events: </h3>
      <div style={{ display: "flex" }}>
        {person_in_List.map((event, index) => (
          <div>
            <Link
              to={`/${user}/${travel}/${travelName}/${event.eventName}`}
              state={{ event: event }}
            >
              <h3 className="link-text" key={index}>
                {event.eventName}
              </h3>
            </Link>
          </div>
        ))}
      </div>
      <br />
    </div>
  );
};

export default Profile;
