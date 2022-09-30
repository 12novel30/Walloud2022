import React, { useState } from "react";
import { Link, useLocation, useParams } from "react-router-dom";
import moment from "moment";

const EventDescription = () => {
  console.log("event description");
  const description = useLocation().state.event;
  console.log("event : ", description);
  const { user, travel, travelName } = useParams();

  const onDelete = () => {
    window.alert("Are you sure you want to delete?");
  };

  const ModifiablePrice = ({ value }) => {
    const [price, setPrice] = useState(value);
    const [dblClicked, setDblClicked] = useState(false);

    console.log(dblClicked);
    const onDoubleClick = () => {
      setDblClicked(true);
    };

    function enterkey() {
      if (window.event.keyCode === 13) {
        if (document.getElementById("price").value !== "") {
          setPrice(document.getElementById("price").value);
        } else {
          setPrice(price);
        }
        setDblClicked(false);
      }
    }

    return (
      <div>
        {dblClicked ? (
          <input
            autoFocus
            type="text"
            id="price"
            onKeyDown={enterkey}
            placeholder={`₩${price}`}
          />
        ) : (
          <h3 onDoubleClick={onDoubleClick}>₩{price}</h3>
          // <h4>₩{price / userList.length} per person</h4> //userList 추가시 인당 가격 추가
        )}
      </div>
    );
  };

  const ModifiableDate = ({ value }) => {
    const [date, setDate] = useState(value);
    const [dblClicked, setDblClicked] = useState(false);

    console.log(dblClicked);
    const onDoubleClick = () => {
      setDblClicked(true);
    };

    function enterkey() {
      if (window.event.keyCode === 13) {
        if (document.getElementById("date").value !== "") {
          setDate(document.getElementById("date").value);
        } else {
          setDate(date);
        }
        setDblClicked(false);
      }
    }

    return (
      <div>
        {dblClicked ? (
          <input
            autoFocus
            id="date"
            type="date"
            onKeyDown={enterkey}
            placeholder={date}
          />
        ) : (
          <h4 onDoubleClick={onDoubleClick}>
            {/* {moment(date).utc().format("YYYY-MM-DD")} */}
            {new Date(date).toISOString().substring(0, 10)}
          </h4>
        )}
      </div>
    );
  };

  return (
    <div>
      <Link to={`/${user}/${travel}/${travelName}`}>
        <h1 className="home">{travelName}</h1>
      </Link>
      <h2 id="headers">{description.name}</h2>
      <div>
        <Link
          to={`/${user}/${travel}/${travelName}/profile/${description.payer}`}
        >
          <h3 className="link-text" id="headers">
            {description.payer}
          </h3>
        </Link>
        <h4>Double Click to Modify, Press Enter to Confirm</h4>
        {/* <h4 id="headers">{description.price}</h4> */}
        <ModifiablePrice value={description.price} />
        {/* <h4 id="headers">
          {moment(description.date).utc().format("YYYY-MM-DD")}
        </h4> */}
        <ModifiableDate value={description.date} />
      </div>
      <div style={{ display: "flex" }}>
        {/* {description.participants.map((participant, id) => (
          <Link
            to={`/${user}/${travel}/${travelName}/profile/${participant}`}
            style={{ margin: "auto" }}
          >
            <h3 className="link-text" key={id}>
              {" "}
              {participant}{" "}
            </h3>
          </Link>
        ))} */}
      </div>

      <button onClick={onDelete}>Delete Event</button>
    </div>
  );
};

export default EventDescription;
