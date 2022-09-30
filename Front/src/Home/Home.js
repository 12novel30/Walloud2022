import React, { useEffect, useState } from "react";
import NavigationBar from "../js/NavigationBar";
import DisplayUsers from "./DisplayUsers";
import Events from "./Events";
import { Link, useNavigate, useParams } from "react-router-dom";
import plusSrc from "../img/plus.jpg";
import axios from "axios";
import { min } from "moment";

const Home = () => {
  // const user = useLocation().state.user_id;
  // const travel = useLocation().state.travel_id;
  const { user, travel, travelName } = useParams();
  const [userList, setuserList] = useState([]);
  const [eventList, seteventList] = useState([]);
  const [period, setPeriod] = useState("");
  const navigate = useNavigate();
  //받아오는 거를 eventList에서 eventlist로 수정
  var userPersonId;
  for (var i = 0; i < userList.length; i++) {
    if (userList[i].userId === parseInt(user)) {
      userPersonId = userList[i].id;
      break;
    }
  }
  ////////////////////////////////////

  useEffect(() => {
    getEventandUser();
  }, []);

  // parameter = user info,
  const getEventandUser = async () => {
    await axios
      .get(`/api/${user}/${travel}`)
      .then((response) => {
        console.log(response.data);
        if (response.data.personCount === 1) {
          navigate(`/${user}/${travel}/${travelName}/createUser`);
        }
        seteventList(response.data.eventList);
        setuserList(response.data.personList);
        setPeriod(response.data.period);
      })
      .catch((error) => {
        console.log(error);
      });
    console.log(userList);
  };

  /////////////////////////////////
  let initialPreferences = {
    displayIcon: true,
    displayMoney: true,
  };

  let [preferences, setPreferences] = useState(initialPreferences);

  localStorage.getItem("preferences") === null
    ? localStorage.setItem("preferences", JSON.stringify(initialPreferences))
    : (preferences = JSON.parse(localStorage.getItem("preferences")));

  return (
    <div>
      <Link
        to={`/${user}/${travel}/${travelName}`}
        state={{ user: user, travel: travel, travelName: travelName }}
      >
        <h1>
          {travelName}
          {" " + period}
        </h1>
      </Link>
      <div className="big-box">
        <h2 className="home-h2">Events</h2>
        {/* <Link to="createEvent" key={(user, travel)}>
              <img className="plus-icon" src={plusSrc} alt="add event" />
            </Link> */}
        <div id="event-box" className="box">
          <div
            style={{
              display: "flex",
              margin: "0",
              borderBottom: "1px solid white",
            }}
          >
            <h4 className="description">Event</h4>
            <h4 className="description">Payer</h4>
            <h4 className="description">Price</h4>
            <h4 className="description">Date</h4>
          </div>
          {eventList.map((event) => (
            <Events event={event} key={event.id}></Events>
          ))}
        </div>
        <Link
          to="createEvent"
          state={{ userList: userList, userPersonId: userPersonId }}
        >
          <button className="home-button">Add Event</button>
        </Link>
      </div>
      <div className="big-box">
        <h2 className="home-h2">Participants</h2>
        {/* <Link to="createUser">
              <img className="plus-icon" src={plusSrc} alt="plus-icon" />
            </Link> */}
        <div id="user-box" className="box">
          <DisplayUsers
            users={userList}
            preferences={preferences}
            travelName={travel}
          />
        </div>
        <Link to="createUser" key={(user, travel)}>
          <button className="home-button">Add User</button>
        </Link>
      </div>
      <div className="right-align">
        <h4>{user}</h4>
        <NavigationBar
          preferences={preferences}
          setPreferences={setPreferences}
        />
      </div>
    </div>
  );
};

export default Home;
