import React, { useEffect, useState } from "react";
import NavigationBar from "../js/NavigationBar";
import DisplayUsers from "./DisplayUsers";
import Events from "./Events";
import { Link, useParams } from "react-router-dom";
import plusSrc from "../img/plus.jpg";
import axios from "axios";
import { min } from "moment";

const Home = () => {
  // const user = useLocation().state.user_id;
  // const travel = useLocation().state.travel_id;
  const { user, travel, travelName } = useParams();
  const [userList, setuserList] = useState([]);
  const [eventList, seteventList] = useState([]);
  //받아오는 거를 eventList에서 eventlist로 수정

  ////////////////////////////////////

  useEffect(() => {
    console.log(user);
    getEventandUser();
  }, []);

  // parameter = user info,
  const getEventandUser = async () => {
    await axios
      .get(`/api/${user}/${travel}`)
      .then((response) => {
        console.log("Resonsed Data : ", response.data);
        seteventList(response.data.eventList);
        setuserList(response.data.personList);
      })
      .catch((error) => {
        console.log(error);
      });
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
        <h1>{travelName}</h1>
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
        <Link to="createEvent" state={{ userList: userList }}>
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

// import React, { useEffect, useState } from "react";
// import NavigationBar from "../js/NavigationBar";
// import DisplayUsers from "./DisplayUsers";
// import Events from "./Events";
// import { Link, useLocation, useParams } from "react-router-dom";
// import plusSrc from "../img/plus.jpg";
// import axios from "axios";
// import { min } from "moment";

// const Home = () => {
//   // const user = useLocation().state.user_id;
//   // const travel = useLocation().state.travel_id;
//   const { user, travel, travelName } = useParams();
//   const [userList, setuserList] = useState([]);
//   const [eventList, seteventList] = useState([]);
//   //받아오는 거를 eventList에서 eventlist로 수정

//   ////////////////////////////////////

//   useEffect(() => {
//     console.log(user);
//     getEventandUser();
//   }, []);

//   // parameter = user info,
//   const getEventandUser = async () => {
//     await axios
//       .get(`/api/${user}/${travel}`)
//       .then((response) => {
//         console.log("Resonsed Data : ", response.data);
//         seteventList(response.data.eventList);
//         setuserList(response.data.personList);
//       })
//       .catch((error) => {
//         console.log(error);
//       });
//   };

//   /////////////////////////////////
//   let initialPreferences = {
//     displayIcon: true,
//     displayMoney: true,
//   };

//   let [preferences, setPreferences] = useState(initialPreferences);

//   localStorage.getItem("preferences") === null
//     ? localStorage.setItem("preferences", JSON.stringify(initialPreferences))
//     : (preferences = JSON.parse(localStorage.getItem("preferences")));

//   var portrait = window.matchMedia("(orientation: portrait)");

//   portrait.addEventListener("change", () => {
//     window.location.reload();
//   });

//   if (portrait.matches) {
//     return (
//       <div>
//         <Link
//           to={`/${user}/${travel}/${travelName}`}
//           state={{ user: user, travel: travel, travelName: travelName }}
//         >
//           <h1>Divide by N</h1>
//         </Link>
//         <div className="big-box">
//           <h2>Events</h2>
//           <div id="event-box" className="box">
//             <div style={{ display: "flex" }}>
//               <h4 className="description">Event</h4>
//               <h4 className="description">Payer</h4>
//               <h4 className="description">Price</h4>
//               <h4 className="description">Date</h4>
//             </div>
//             <hr />
//             {eventList.map((event) => (
//               <Events event={event} key={event.id}></Events>
//             ))}
//           </div>
//           <Link to="createEvent" state={{ userList: userList }}>
//             <button>Add Event</button>
//           </Link>
//         </div>
//         <div className="big-box">
//           <h2>Participants</h2>
//           <div id="user-box">
//             <DisplayUsers
//               users={userList}
//               preferences={preferences}
//               travelName={travel}
//             />
//           </div>
//           <Link to="createUser" key={(user, travel)}>
//             <button>Add User</button>
//           </Link>
//         </div>
//         <div className="right-align">
//           <h4>{user}</h4>
//           <NavigationBar
//             preferences={preferences}
//             setPreferences={setPreferences}
//           />
//         </div>
//       </div>
//     );
//   } else {
//     return (
//       <div>
//         <Link
//           to={`/${user}/${travel}/${travelName}`}
//           state={{ user: user, travel: travel, travelName: travelName }}
//         >
//           <h1>Divide by N</h1>
//         </Link>
//         <div style={{ display: "flex", width: "90%" }}>
//           <h2 className="home-h2">Events</h2>
//           <h2 className="home-h2">Participants</h2>
//         </div>

//         <div style={{ display: "flex", width: "90%" }}>
//           <div id="event-box" className="box">
//             <div style={{ display: "flex" }}>
//               <h4 className="description">Event</h4>
//               <h4 className="description">Payer</h4>
//               <h4 className="description">Price</h4>
//               <h4 className="description">Date</h4>
//             </div>
//             <hr />
//             {eventList.map((event) => (
//               <Events event={event} key={event.id}></Events>
//             ))}
//           </div>
//           <div id="user-box">
//             <DisplayUsers
//               users={userList}
//               preferences={preferences}
//               travelName={travel}
//             />
//           </div>
//         </div>

//         <div style={{ width: "90%" }}>
//           <Link to="createEvent" state={{ userList: userList }}>
//             <button className="home-button">Add Event</button>
//           </Link>
//           <Link to="createUser" key={(user, travel)}>
//             <button className="home-button">Add User</button>
//           </Link>
//         </div>
//       </div>
//     );
//   }
// };

// export default Home;
