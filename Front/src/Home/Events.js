import React from "react";
import { Link, useParams } from "react-router-dom";
import moment from "moment/moment";
import { useLocation } from "react-router-dom";

function Events({ event, userList }) {
  const { user, travel, travelName } = useParams();
  return (
    <div className="event-row">
      <Link
        className="event"
        to={`/${user}/${travel}/${travelName}/${event.name}`}
        state={{ event: event, users: userList }}
      >
        <span className="link-text">{event.name}</span>
      </Link>
      <span className="event">{event.payerName}</span>
      <span className="event">₩{event.price}</span>
      <span className="event">{moment(event.date).utc().format("MM/DD")}</span>
    </div>
  );
}

export default Events;
