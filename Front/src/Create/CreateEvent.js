import axios from "axios";
import { normalizeUnits } from "moment";
import React, { useState } from "react";
import { useLocation, Link, useParams, useNavigate } from "react-router-dom";
import personSrc from "../img/person.png";

function CreateEvent() {
  const users = useLocation().state.userList;
  const userPersonId = useLocation().state.userPersonId;
  const { user, travel, travelName } = useParams();
  const [payer, setPayer] = useState(userPersonId);

  const [participants, setParticipants] = useState([...users]);
  const navigate = useNavigate();

  const [inputs, setInputs] = useState({
    place: "",
    price: "",
    date: new Date().toISOString().substring(0, 10),
  });
  const { place, price, date } = inputs;

  const onChange = (e) => {
    const { value, name } = e.target;
    console.log(value, name);
    setInputs({
      ...inputs,
      [name]: value,
    });
  };

  const checkHandler = (checked, elem) => {
    if (checked) {
      setParticipants((prev) => [...prev, elem]);
      console.log(elem, "push", participants);
    } else {
      setParticipants(participants.filter((e) => e !== elem));
    }
  };

  const payer_in_parti = () => {
    return (
      participants.length === participants.filter((e) => e.id !== payer).length
    );
  };

  const onSubmit = (e) => {
    if (place === "") {
      alert("Set place\n");
    } else if (price === "") {
      alert("Set price\n");
      // } else if (date === "") {
      // alert("Set date\n");
    } else if (payer_in_parti()) {
      alert("payer should be in participants");
    } else {
      console.log(participants);
      console.log("payer : ", payer);
      console.log("participant : ", participants);
      // event_info();
    }
  };

  const setSelectedPayer = (e) => {
    setPayer(e.target.value);
    document.getElementById(payer).disabled = false;
    document.getElementById(e.target.value).disabled = true;
  };

  const event_info = async () => {
    let temp_list = [...participants].map(function (row) {
      delete row.name;
      delete row.difference;
      delete row.userId;

      return row;
    });

    console.log("event json", {
      parti_list: temp_list,
      event_name: place,
      event_date: date,
      price: price,
      payer_person_id: payer,
    });

    // await axios
    //   .post(`/api/${user}/${travel}/CreateEvent`, {
    //     parti_list: temp_list,
    //     event_name: place,
    //     event_date: date,
    //     price: price,
    //     payer_person_id: payer,
    //   })
    //   .then((res) => {
    //     switch (res.data) {
    //       case -1:
    //         alert("fail to create event");
    //         break;
    //       case -2:
    //         alert("fail to create participate");
    //         break;
    //       case 200:
    //         alert("Success");
    //         navigate(`/${user}/${travel}/${travelName}`);
    //         break;
    //       default:
    //         throw "Network Error";
    //     }
    //   })
    //   .catch((error) => {
    //     console.log(error);
    //   });
  };

  return (
    <div>
      <Link
        to={`/${user}/${travel}/${travelName}`}
        // state={{ user_id: userid, travel_id: travelid, travelName: travelname }}
      >
        <h1 className="home">{travelName}</h1>
      </Link>
      <h2>Create Event</h2>
      <div>
        <label htmlFor="place">Place</label>
        <input
          type="text"
          id="place"
          name="place"
          onChange={onChange}
          size="5"
          autoFocus
        />
        <label htmlFor="price">Price</label>
        <input
          type="text"
          id="price"
          name="price"
          onChange={onChange}
          size="5"
        />
        <label htmlFor="date">Date</label>
        <input
          type="date"
          id="date"
          name="date"
          onChange={onChange}
          size="5"
          defaultValue={new Date().toISOString().substring(0, 10)}
        />
      </div>
      {/* <label htmlFor="create-event">Participants</label> */}
      {/* <div className="box" id="create-event">
        {users.map((user) => (
          <CreateUser each={user} key={user.id} />
        ))}
      </div> */}
      <div>
        <label htmlFor="participants">Payer</label>
        <select id="participants" onChange={setSelectedPayer}>
          {users.map((userInfo, id) =>
            parseInt(user) === parseInt(userInfo.userId) ? (
              <option value={userInfo.id} key={id} selected>
                {userInfo.name}
              </option>
            ) : (
              <option value={userInfo.id} key={id}>
                {userInfo.name}
              </option>
            )
          )}
        </select>
      </div>
      <label>Participants</label>
      <br />
      <div
        style={{
          width: "400px",
          marginTop: "2%",
          verticalAlign: "center",
        }}
      >
        {users.map((userInfo, id) => (
          <div
            style={{
              display: "inline-block",
              minWidth: "33%",
              alignItems: "center",
              marginBottom: "3%",
            }}
            key={id}
          >
            <input
              className="checkbox"
              defaultChecked
              type="checkbox"
              id={userInfo.id}
              onChange={(e) => checkHandler(e.target.checked, userInfo)}
              disabled={userInfo.id === userPersonId}
            />
            <label className="checkbox-text" htmlFor={userInfo.id}>
              {userInfo.name}
            </label>
          </div>
        ))}
      </div>
      <button onClick={onSubmit}>이벤트 추가</button>
    </div>
  );
}

export { CreateEvent };
