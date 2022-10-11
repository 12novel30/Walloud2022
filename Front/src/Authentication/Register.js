import axios from "axios";
import { React, useState } from "react";
import { Link } from "react-router-dom";

const Register = () => {
  const [user_name, setname] = useState("");
  const [user_account, setaccout] = useState("");
  const [user_email, setEmail] = useState("");
  const [user_password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const onUserHandler = (event) => {
    setname(event.currentTarget.value);
  };

  const onAccountHandler = (event) => {
    setaccout(event.currentTarget.value);
  };

  const onEmailHandler = (event) => {
    setEmail(event.currentTarget.value);
  };

  const onPasswordHandler = (event) => {
    setPassword(event.currentTarget.value);
  };

  const onConfirmPasswordHandler = (event) => {
    setConfirmPassword(event.currentTarget.value);
  };

  const CreateUser = async () => {
    await axios
      .post("/api/Register", {
        user_name: user_name,
        user_email: user_email,
        user_password: user_password,
        user_account: user_account,
      })
      .then((response) => {
        console.log(response);
        setname("");
        setEmail("");
        setaccout("");
        setPassword("");
        setConfirmPassword("");
      })
      .catch((error) => {
        // if (error.response.data.status === 500) {
        //   alert(error.response.data.message);
        // }
        console.log(error);
      });
  };

  const onSubmit = (event) => {
    if (user_email == null) {
      event.preventDefault();
      alert("Given ID already exists");
    } else if (user_password !== confirmPassword) {
      event.preventDefault();
      alert("Passwords do not match");
    } else if (user_password.length < 5) {
      event.preventDefault();
      alert("Password is too short");
    } else {
      console.log(user_name, user_account, user_email, user_password);
      CreateUser();
    }
  };

  function enterkey() {
    if (window.event.keyCode == 13) {
      onSubmit();
    }
  }

  return (
    <div>
      <h1>Divide by N</h1>
      <h2>Register</h2>
      <form style={{ margin: "10px auto", textAlign: "center" }}>
        <div>
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            value={user_email}
            onChange={onEmailHandler}
            autoFocus
            required
          />
        </div>
        <div>
          <label htmlFor="name">Name</label>
          <input
            type="text"
            id="name"
            value={user_name}
            onChange={onUserHandler}
            required
          />
        </div>
        <div>
          <label htmlFor="account">Account</label>
          <input
            type="text"
            id="account"
            value={user_account}
            onChange={onAccountHandler}
            required
          />
        </div>
        <div>
          <label htmlFor="password">Password</label>
          <input
            type="password"
            id="password"
            value={user_password}
            onChange={onPasswordHandler}
            required
          />
        </div>
        <div>
          <label htmlFor="confirm-password" onKeyDown={enterkey}>
            Confirm Password
          </label>
          <input
            type="password"
            id="confirm-password"
            value={confirmPassword}
            onChange={onConfirmPasswordHandler}
            required
          />
        </div>
        <Link to="/login">
          <div>
            <button type="submit" onKeyDown={enterkey} onClick={onSubmit}>
              Submit
            </button>
          </div>
        </Link>
      </form>
    </div>
  );
};

export default Register;
