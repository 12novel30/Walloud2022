import axios from "axios";
import { normalizeUnits } from "moment";
import React, { useState } from "react";
import { useLocation, Link, useParams, useNavigate } from "react-router-dom";
import personSrc from "../img/person.png";

function ChangeEvent() {
  const description = useLocation().state.description;
  const users = useLocation().state.users;
  const eventId = useLocation().state.eventId;
  const { user, travel, travelName } = useParams();

  const parti_list_id = useLocation().state.parti_list.map((e) => e.personId);
  console.log("parti", parti_list_id);
  var participants = users.filter((e) => parti_list_id.includes(e.personId));
  console.log("init", participants);
  var payer = users.filter((e) => e.name === description.payerName)[0].personId;
  const navigate = useNavigate();
  var place = description.name;
  var price = description.price;
  var date = description.date.substring(0, 10);

  const checkHandler = (checked, elem) => {
    if (checked) {
      participants.push(elem);
      console.log(elem, "push", participants);
    } else {
      participants = participants.filter((e) => e.personId !== elem.personId);
    }
    console.log(participants);
  };

  const onSubmit = (e) => {
    place = document.querySelector("#place").value;
    price = document.querySelector("#price").value;
    date = document.querySelector("#date").value.substring(0, 10);
    if (place === "") {
      alert("Set place\n");
    } else if (price === "") {
      alert("Set price\n");
    } else {
      console.log("payer : ", payer);
      console.log("participant : ", participants);
      event_info();
    }
  };

  const setSelectedPayer = (e) => {
    payer = e.target.value;
    console.log(payer);
  };

  const event_info = async () => {
    let temp_list = [...participants].map(function (row) {
      delete row.name;
      delete row.difference;
      delete row.userId;
      row.spent = document.getElementById(`${row.personId}-spent`).value;

      return row;
    });

    console.log("event json", {
      parti_list: temp_list,
      event_name: place,
      event_date: date,
      price: price,
      payer_person_id: payer,
    });

    // ========= 수정 필요!!! =======
    await axios
      .post(`/api/${user}/${travel}/${eventId}/updateEvent`, {
        parti_list: temp_list,
        event_name: place,
        event_date: date,
        price: price,
        payer_person_id: payer,
      })

      .then((res) => {
        switch (res.data) {
          case -1:
            alert("fail to change event");
            break;
          case -2:
            alert("fail to change participate");
            break;
          case 200:
            alert("Success");
            navigate(`/${user}/${travel}/${travelName}`, {
              state: { created: false },
            });
            break;
          default:
            throw "Network Error";
        }
      })
      .catch((error) => {
        console.log(error);
      });
  };

  //======

  return (
    <div>
      <Link to={`/${user}/${travel}/${travelName}`} state={{ created: false }}>
        <h1 className="home">{travelName}</h1>
      </Link>
      <h2>Change Event</h2>
      <div>
        <label htmlFor="place">Place</label>
        <input
          type="text"
          id="place"
          name="place"
          defaultValue={description.name}
          size="5"
        />
        <label htmlFor="price">Price</label>
        <input
          type="text"
          id="price"
          name="price"
          defaultValue={description.price}
          size="5"
        />
        <label htmlFor="date">Date</label>
        <input
          type="date"
          id="date"
          name="date"
          size="5"
          defaultValue={description.date.substring(0, 10)}
        />
      </div>
      <div>
        <label htmlFor="participants">Payer</label>
        <select id="participants" onChange={setSelectedPayer}>
          {users.map((userInfo, id) =>
            userInfo.name === description.payerName ? (
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
              defaultChecked={parti_list_id.includes(userInfo.personId)}
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
            />
          </div>
        ))}
      </div>
      <button onClick={onSubmit}>변경 사항 저장</button>
    </div>
  );
}

export { ChangeEvent };
