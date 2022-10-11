import axios from "axios";
import { normalizeUnits } from "moment";
import React, { useState } from "react";
import { useLocation, Link, useParams, useNavigate } from "react-router-dom";
import personSrc from "../img/person.png";

function CreateEvent() {
  const users = useLocation().state.userList;
  const userPersonId = useLocation().state.userPersonId;
  const { user, travel, travelName } = useParams();
  const [payer, setPayer] = useState(users[0].personId)

  const [participants, setparti] = useState([...users]);
  const navigate = useNavigate();

  const [inputs, setInputs] = useState({
    place: "",
    price: "",
    date: new Date().toISOString().substring(0, 10),
  });
  const { place, price, date } = inputs;

  const onChange = (e) => {
    const { value, name } = e.target;
    setInputs({
      ...inputs,
      [name]: value,
    });
  };

  const checkHandler = (checked, elem) => {
    console.log(elem)
    if (checked) {
      // participants.push(elem);
      setparti([...participants,elem]);
    } else {
      // participants.filter((e) => e.personId !== elem.personId);
      var temp = [...participants].filter((e)=> e.personId !== elem.personId);
      setparti(temp);
    }
  };

  const onSubmit = (e) => {

    if (place === "") {
      alert("Set place\n");
    } else if (price === "") {
      alert("Set price\n");
    } else {
      event_info();
    }
  };

  const setSelectedPayer = (e) => {
    setPayer(e.target.value)
  };

  const event_info = async () => {
    let temp_list = [];
    for (let i = 0; i < participants.length; i++) {
      let t_elem = {
        role : participants[i].role,
        id : participants[i].personId
      }
      temp_list.push(t_elem);
    }

    console.log("event json", {
      parti_list: temp_list,
      event_name: place,
      event_date: date,
      price: price,
      payer_person_id: payer,
    });

    await axios
      .post(`/api/${user}/${travel}/CreateEvent`, {
        parti_list: temp_list,
        event_name: place,
        event_date: date,
        price: price,
        payer_person_id: payer,
      })
      .then(() => {
        navigate(`/${user}/${travel}/${travelName}`, {
          state: { created: false },
        });
      })
      .catch((error) => {
        if (error.response.data.status === 500) {
          alert(error.response.data.message);
        }
      });
  };

  return (
    <div>
      <Link to={`/${user}/${travel}/${travelName}`} state={{ created: false }}>
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
              <option value={userInfo.personId} key={id} selected>
                {userInfo.name}
              </option>
            ) : (
              <option value={userInfo.personId} key={id}>
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
              id={userInfo.personId}
              onChange={(e) => checkHandler(e.target.checked, userInfo)}
            />
            <label className="checkbox-text" htmlFor={userInfo.personId}>
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