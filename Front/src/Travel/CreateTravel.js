import axios from "axios";
import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

const CreateTravel = (props) => {
  console.log(props);
  const navigate = useNavigate();
  const travel_List = props.myTravel;
  const user_id = props.user;
  const duplicate = false;
  const [Travel_name, setTravel_name] = useState("");

  const onChange = (event) => {
    setTravel_name(event.currentTarget.value);
  };

  const onSubmit = () => {
    if (Travel_name === "") {
      alert("Travel Name is blank!");
    }

    for (let i = 0; i < travel_List.length; i++) {
      if (travel_List[i].name === Travel_name) {
        alert("Travel Name exist");
        return;
      }
    }
    axios
      .post(`/api/${user_id}/createTravel`, { travel_name: Travel_name })
      .then(() => {
        window.location.reload();
      })
      .catch((error) => {
        console.log(error);
      });
  };

  function enterkey() {
    if (window.event.keyCode == 13) {
      onSubmit();
    }
  }

  return (
    <div>
      <br />
      <label for="travel-name">New Travel</label>
      <input
        onKeyDown={enterkey}
        onChange={onChange}
        value={Travel_name}
        id="travel-name"
      />
      <button
        style={{ display: "block", margin: "20px auto" }}
        onClick={onSubmit}
      >
        Create Travel
      </button>
    </div>
  );
};

export default CreateTravel;