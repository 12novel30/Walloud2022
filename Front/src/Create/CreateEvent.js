import axios from "axios";
import { normalizeUnits } from "moment";
import React, { useEffect, useState } from "react";
import { useLocation, Link, useParams, useNavigate } from "react-router-dom";
import personSrc from "../img/person.png";

function CreateEvent() {
  const users = useLocation().state.userList;
  const userPersonId = useLocation().state.userPersonId;
  const { user, travel, travelName } = useParams();
  const [payer, setPayer] = useState(users[0].personId);
  const navigate = useNavigate();

  const [price, setPrice] = useState(0);
  var place = "";
  var date = new Date().toISOString().substring(0, 10);
  const onChangePrice = (e) => {
    setPrice(() => e.target.value);
  };

  const [participants, setParticipants] = useState([...users]);

  const changeSpent = (elem) => {
    const parti_id = participants.map((e) => e.personId);
    users.map((e) => {
      if (parti_id.includes(parseInt(e.personId)) && !e.modified) {
        document.getElementById(`${e.personId}-spent`).disabled = false;
        document.getElementById(`${e.personId}-spent`).value = Math.round(
          price / participants.filter((e) => !e.modified).length
        );
      } else if (e.modified) {
      } else {
        document.getElementById(`${e.personId}-spent`).disabled = true;
        document.getElementById(`${e.personId}-spent`).value = 0;
      }
    });
  };
  const checkHandler = (checked, elem) => {
    if (checked) {
      // participants.push(elem);
      setParticipants(() => [...participants, elem]);
    } else {
      // participants = participants.filter((e) => e.personId !== elem.personId);
      setParticipants(() =>
        [...participants].filter((e) => e.personId !== elem.personId)
      );
    }
  };

  const onChangeSpent = (elem) => {};

  useEffect(() => {
    changeSpent();
  }, [price, participants]);

  const onSubmit = (e) => {
    place = document.querySelector("#place").value;
    date = document.querySelector("#date").value;
    if (place === "") {
      alert("Set place\n");
    } else if (parseInt(price) === 0) {
      alert("Set price\n");
    } else {
      event_info();
    }
  };

  const setSelectedPayer = (e) => {
    setPayer(e.target.value);
  };

  const event_info = async () => {
    let total_sum = 0;
    let temp_list = [...participants].map(function (row) {
      row.spent = document.getElementById(`${row.personId}-spent`).value;
      row.role = row.personId === parseInt(payer);

      total_sum = total_sum + parseInt(row.spent);
      return row;
    });

    console.log(total_sum);

    if (total_sum !== parseInt(price) && total_sum + 1 !== parseInt(price)) {
      alert("Participant's sum is not same as price");
      return;
    }

    console.log("event json", {
      parti_list: temp_list,
      event_name: place,
      event_date: date,
      price: price,
      payer_person_id: payer,
    });

    temp_list = temp_list.map(function(row) {
      delete row.name;
      delete row.difference;
      delete row.userId;

      return row;
    })

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
        else {
          alert("Check The network");
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
        <input type="text" id="place" name="place" size="5" autoFocus />
        <label htmlFor="price">Price</label>
        <input
          type="text"
          id="price"
          name="price"
          size="5"
          onChange={onChangePrice}
        />
        <label htmlFor="date">Date</label>
        <input
          type="date"
          id="date"
          name="date"
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
              width: "100%",
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
            <label
              className="checkbox-text"
              htmlFor={userInfo.personId}
              style={{ width: "30%" }}
            >
              {userInfo.name}
            </label>
            <input
              id={`${userInfo.personId}-spent`}
              style={{ display: "inline-block", width: "40%" }}
              onChange={onChangeSpent}
            />
          </div>
        ))}
      </div>
      <button onClick={onSubmit}>이벤트 추가</button>
    </div>
  );
}

export { CreateEvent };
