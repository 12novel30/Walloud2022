import axios from "axios";
import React from "react";
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import styled from "styled-components";

const Container = styled.div`
  margin-top: 50px;
  padding: 20px;
`;

const Input = styled.input`
  position: relative;
  overflow: hidden;
  width: 100%;
  height: 40px;
  margin: 0 0 8px;
  padding: 5px 39px 5px 11px;
  border: solid 1px #dadada;
  background: #fff;
  box-sizing: border-box;
`;

const Button = styled.div`
  font-size: 18px;
  font-weight: 700;
  line-height: 49px;
  display: block;
  width: 100%;
  height: 49px;
  margin: 16px 0 7px;
  cursor: pointer;
  color: #fff;
  border: none;
  border-radius: 0;
  background-color: #03c75a;
  ${({ disabled }) =>
    disabled &&
    `
    background-color: #efefef;
  `}
`;

const LogIn = () => {
  const [input_id, setId] = useState("");
  const [input_password, setPassword] = useState("");
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
        switch (response.data) {
          case 0:
            throw "Network Error";
          case -1:
            alert("Wrong Password");
            navigate("/login");
            break;
          case -2:
            alert("Wrong Email");
            navigate("/login");
            break;
          default:
            alert("Login Success!");
            navigate("/selectTravel", { state: { id: response.data } });
            break;
        }
      })
      .catch((error) => {
        console.log(error);
        alert("Error");
      });
  };

  const onSubmit = (event) => {
    try_LogIn(event);
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
