import React, { useEffect, useState } from "react";
import NavigationBar from "../js/NavigationBar";
import DisplayUsers from "./DisplayUsers";
import Events from "./Events";
import { Link, useLocation, useNavigate, useParams } from "react-router-dom";
import plusSrc from "../img/plus.png";
import axios from "axios";
import { min } from "moment";
import './topBar.css'

const Home = () => {
  // const user = useLocation().state.user_id;
  // const travel = useLocation().state.travel_id;

  const [created, setCreate] = useState(useLocation().state.created);
  const { user, travel, travelName } = useParams();
  const [userList, setuserList] = useState([]);
  const [eventList, seteventList] = useState([]);
  const [period, setPeriod] = useState("");
  const [su_personId, setSu_Person] = useState(-1);
  const [su, setSu] = useState(false);
  const [mod, setMod] = useState(false);
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

  const goHome = () => {
    navigate(`/${user}/${travel}/${travelName}`, { state: { created: false } });
  };
  // parameter = user info,
  const getEventandUser = async () => {
    await axios
      .get(`/api/${user}/${travel}`)
      .then((response) => {
        console.log(response.data);
        if (created) {
          if (window.confirm("여행 참가자가 있습니까?")) {
            navigate(`/${user}/${travel}/${travelName}/createUser`);
          } else {
            setCreate(false);
          }
        }
        seteventList(response.data.eventList);
        setuserList(response.data.personList);
        setPeriod(response.data.period);
        setSu_Person(response.data.superUser);
        let su_personId = response.data.superUser;
        response.data.personList.map((e) => {
          if (e.personId === su_personId && e.userId === parseInt(user)) {
            setSu(true);
            console.log("true");
          }
        })
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const updateTravel = () => {
    if(!mod) {
      setMod(true);
    }else {
      setMod(false);
      axios.put(`/api/${user}/${travel}/updateTravelInfo`,{
        travel_name : document.getElementById("travel-name").value
      }).then((res) => {
        navigate(`/${user}/${travel}/${res.data.name}`,{ state: { created: false } })
      }).catch((error) => {
        console.log(error);
      })
    }
  }

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
      <div className="topBar">
        {mod ? 
          <input defaultValue={travelName} id="travel-name"/>
        : <h1 onClick={goHome}>
          {travelName}
        </h1>}
        {period === null ? <div></div> : <div className="period">{" " + period}</div>}
        {su ? <div>
          <button onClick={updateTravel}>Modify Name</button>
        </div> : <></>}
      </div>
      <div className="big-box">
        <h2 className="home-h2">Event</h2>
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
            <h4 className="description">Price</h4>
            <h4 className="description">Payer</h4>
            <h4 className="description">Date</h4>
          </div>
          {eventList.map((event) => (
            <Events event={event} userList={userList} key={event.id}></Events>
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
        <h2 className="home-h2">People</h2>
        {/* <Link to="createUser">
              <img className="plus-icon" src={plusSrc} alt="plus-icon" />
            </Link> */}
        <div id="user-box" className="box">
          <DisplayUsers
            users={userList}
            preferences={preferences}
            travelName={travel}
            su_pid = {su_personId}
          />
        </div>
        <Link to="createUser" key={(user, travel)}>
          <button className="home-button">Add User</button>
        </Link>
      </div>
      <div>
        <NavigationBar
          preferences={preferences}
          setPreferences={setPreferences}
        />
      </div>
    </div>
  );
};

export default Home;
