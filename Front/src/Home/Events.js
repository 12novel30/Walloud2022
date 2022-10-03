import React from "react";
import { Link, useParams } from "react-router-dom";
import moment from "moment/moment";

function Events({ event, users }) {
  const { user, travel, travelName } = useParams();
  return (
    <div className="event-row">
      <Link
        className="event"
        to={`/${user}/${travel}/${travelName}/${event.name}`}
        state={{ event: event, users: users }}
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
