import axios from "axios";
import React, { useState, useEffect } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";

const Profile = () => {
  const { user, travel, travelName, personId } = useParams();
  const navigate = useNavigate();
  const [profile, setProfile] = useState({});
  const [person_in_List, seteventList] = useState([]);

  useEffect(() => {
    getProfile();
  }, []);

  const getProfile = async () => {
    await axios
      .get(`/api/${user}/${travel}/${personId}/personDetail`)
      .then((res) => {
        console.log(res.data);
        setProfile(res.data);
        seteventList(res.data.eventList);
        console.log(person_in_List);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const delPerson = async () => {
    if (
      window.confirm(
        "Username " +
          profile.userName +
          "should be not in any events\nAre you sure you want to delete?"
      )
    ) {
      await axios
        .delete(`/api/${user}/${travel}/${personId}/deleteUser`, {
          person_id: personId,
        })
        .then((res) => {
          console.log(res.data);
          navigate(`/${user}/${travel}/${travelName}`, {state : { created: false }})
        })
        .catch((error) => {
          console.log(error);
        });
    }
  };

  return (
    <div>
      <Link to={`/${user}/${travel}/${travelName}`} state={{ created: false }}>
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
      <button onClick={delPerson}>Delete person</button>
      <br />
    </div>
  );
};

export default Profile;
