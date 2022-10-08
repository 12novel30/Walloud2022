import React, { useEffect, useState } from "react";
import { Link, useLocation, useNavigate, useParams } from "react-router-dom";
import moment from "moment";
import axios from "axios";

const EventDescription = () => {
  const users = useLocation().state.users;
  console.log("event description", users);
  const description = useLocation().state.event;
  const navigate = useNavigate();
  console.log("event : ", description);
  const { user, travel, travelName } = useParams();
  const [parti_list, setParti] = useState([]);

  const onDelete = async () => {
    if (window.confirm("Are you sure you want to delete?")) {
      await axios
        .delete(`/api/${user}/${travel}/${description.eventId}/deleteEvent`)
        .then((res) => {
          console.log(res.data);
          window.alert("Succesfully Deleted");
          navigate(`/${user}/${travel}/${travelName}`, {
            state: { created: false },
          });
        })
        .catch((error) => {
          console.log(error);
          window.location.reload();
        });
    }
  };

  useEffect(() => {
    axios
      .get(`/api/${user}/${travel}/${description.eventId}/detail`)
      .then((res) => {
        console.log(res.data);
        setParti(res.data);
      });
  }, []);

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
          <div>
            <h3
              style={{ display: "inline-block", margin: "0" }}
              onDoubleClick={onDoubleClick}
            >
              {" "}
              ₩{price}
            </h3>
            <h4 style={{ display: "inline-block", margin: "0" }}>
              ({description.payerName})
            </h4>
            <h5>₩{Math.round(price / parti_list.length)} per person</h5>
            {/* 추가시 인당 가격 추가 */}
          </div>
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
      <Link to={`/${user}/${travel}/${travelName}`} state={{ created: false }}>
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
        <h5>Double Click to Modify, Press Enter to Confirm</h5>
        <ModifiablePrice value={description.price} />
        <ModifiableDate value={description.date} />
        {/* <h4>{description.payerName}</h4> */}
      </div>
      <div style={{ display: "flex" }}></div>
      <div style={{ display: "flex" }}>
        {parti_list.map((parti, index) => (
          <div>
            <Link
              to={`/${user}/${travel}/${travelName}/profile/${parti.name}`}
              state={{ personid: parti.id }}
            >
              <h3 className="link-text" key={index}>
                {parti.name}
              </h3>
            </Link>
          </div>
        ))}
      </div>
      <button onClick={onDelete}>Delete Event</button>
      <Link
        to={`/${user}/${travel}/${travelName}/changeEvent`}
        state={{
          description: description,
          users: users,
          parti_list: parti_list,
        }}
      >
        <button>Change</button>
      </Link>
    </div>
  );
};

export default EventDescription;
