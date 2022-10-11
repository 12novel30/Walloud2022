import axios from "axios";
import React from "react";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import styled from "styled-components";

const LogIn = () => {
  const [input_id, setId] = useState("");
  const [input_password, setPassword] = useState("");
  const [passwordCheck, setPasswordCheck] = useState("none");
  const navigate = useNavigate();

  const onIdHandler = (event) => {
    setId(event.currentTarget.value);
  };

  const onPasswordHandler = (event) => {
    setPassword(event.currentTarget.value);
  };

  const try_LogIn = async () => {
    await axios
      .post("/api/login", {
        input_id: input_id,
        input_password: input_password,
      })
      .then((response) => {
        console.log(response.data);
        alert("Login Success!");
        navigate("/selectTravel", { state: { id: response.data } });
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

  const onSubmit = (event) => {
    try_LogIn(event)
  };

  const enterkey = () => {
    if (window.event.keyCode == 13) {
      onSubmit();
    }
  };

  return (
    <div className="login">
      <h1>Divide by N</h1>
      <h2>Log In</h2>
      <label htmlFor="email">Email</label>
      <input
        type="email"
        name="email"
        id="email"
        value={input_id}
        onChange={onIdHandler}
        required
        autoFocus
      />
      <label htmlFor="password">Password</label>
      <input
        type="password"
        name="password"
        id="password"
        value={input_password}
        onChange={onPasswordHandler}
        onKeyDown={enterkey}
        required
        // autoFocus
      />
      <h5 style={{ display: passwordCheck, color: "red" }}>
        Please check your password
      </h5>
      <button id="log-in" type="submit" onClick={onSubmit}>
        Log In
      </button>
      <h5 style={{ margin: "5rem 0 0 0 " }}>
        If you don't have ID, register first
      </h5>
      <Link to="/register">
        <button>Register</button>
      </Link>
    </div>
  );
};

export default LogIn;
