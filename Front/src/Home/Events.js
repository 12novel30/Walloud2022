import React from "react";
import { Link, useParams } from "react-router-dom";
import moment from "moment/moment";

function Events({ event }) {
  const { user, travel, travelName } = useParams();
  return (
    <div className="event-row">
      <Link
        className="event"
        to={`/${user}/${travel}/${travelName}/${event.name}`}
        state={{ event: event }}
      >
        <span className="link-text">{event.name}</span>
      </Link>
      <span className="event">{event.payerName}</span>
      <span className="event">₩{event.price}</span>
      <span className="event">{moment(event.date).utc().format("MM/DD")}</span>
    </div>
  );
}

// function Events({ event }) {
//   const { user, travel, travelName } = useParams();
//   var table = document.getElementById("event");
//   var newRow = table.insertRow(0);

//   var name = newRow.insertCell(0);
//   var payer = newRow.insertCell(1);
//   var price = newRow.insertCell(2);
//   var date = newRow.insertCell(3);

//   name.innerHTML = event.name;
//   payer.innerHTML = event.payer;
//   price.innerHTML = event.price;
//   date.innerHTML = event.date;
//   return (
//     // <table id="event">
//     //   <tr style={{ display: "flex" }}>
//     //     <Link
//     //       className="event"
//     //       to={`/${user}/${travel}/${travelName}/${event.name}`}
//     //       state={{ event: event }}
//     //     >
//     //       <td className="link-text">{event.name}</td>
//     //     </Link>
//     //     <td className="event">{event.payer}</td>
//     //     <td className="event">{event.price}</td>
//     //     <td className="event">
//     //       {moment(event.date).utc().format("YYYY-MM-DD")}
//     //     </td>
//     //   </tr>
//     // </table>
//     <></>
//   );
// }

export default Events;
