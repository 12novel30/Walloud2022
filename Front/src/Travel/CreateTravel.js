import axios from "axios";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const CreateTravel = (props) => {
  console.log(props);
  const travel_List = props.myTravel;
  const user_id = props.user;
  const [Travel_name, setTravel_name] = useState("");
  const navigate = useNavigate();

  const onChange = (event) => {
    setTravel_name(event.currentTarget.value);
  };

  const onSubmit = () => {
    if (Travel_name === "") {
      alert("Travel Name is blank!");
    } else {
      for (let i = 0; i < travel_List.length; i++) {
        if (travel_List[i].name === Travel_name) {
          alert("Travel Name exist");
          return;
        }
      }
      axios
        .post(`/api/${user_id}/createTravel`, { travel_name: Travel_name })
        .then((res) => {
          console.log(res.data);
          navigate(`/${user_id}/${res.data}/${Travel_name}/`, {
            state: { created: true },
          });
        })
        .catch((error) => {
          if (error.response.data.status === 500) {
            alert(error.response.data.message);
          }
        });
    }
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
        style={{ width: "50%" }}
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
