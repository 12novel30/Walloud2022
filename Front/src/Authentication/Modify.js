import axios from "axios";
import Bank from "./Bank";
import { React, useState } from "react";
import { Link, useLocation, useNavigate, useParams } from "react-router-dom";

const Modify = () => {
  const userinfo = useLocation().state.user_info;
  const {user} = useParams();
  const navigate = useNavigate();
  const [user_name, setname] = useState(userinfo.name);
  const [user_account, setaccout] = useState(userinfo.account);
  const [user_email, setEmail] = useState(userinfo.email);
  const [user_password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const onUserHandler = (event) => {
    setname(event.currentTarget.value);
  };

  const onAccountHandler = (event) => {
    setaccout(event.currentTarget.value);
  };


  const onPasswordHandler = (event) => {
    setPassword(event.currentTarget.value);
  };

  const onConfirmPasswordHandler = (event) => {
    setConfirmPassword(event.currentTarget.value);
  };

  const CreateUser = async () => {
    await axios
      .put(`/api/${user}/updateUserInfo`, {
        user_name: user_name,
        user_email: user_email,
        user_password: user_password,
        user_account: user_account,
        user_bank:document.getElementById("Bank").value
      })
      .then(() => {
        navigate('/selectTravel', {state : {id: user}})
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
    if (user_email == null) {
      event.preventDefault();
      alert("Given ID already exists");
    } else if (user_password !== confirmPassword) {
      event.preventDefault();
      alert("Passwords do not match");
    } else if (user_password.length < 5) {
      event.preventDefault();
      alert("Password is too short");
    }else {
      event.preventDefault();
      CreateUser();
    }
  };

  const onDelete = async(event) => {
    event.preventDefualt();
    await axios.delete(`/api/${user}/deregister`)
    .then(
      () => {
        navigate('/login');
      }
    ).catch((error) => {
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      }
      else {
        alert("Check The network");
      }
    })
  }

  function enterkey() {
    if (window.event.keyCode == 13) {
      onSubmit();
    }
  }

  return (
    <div>
      <h1>Divide by N</h1>
      <h2>Modify</h2>
      <form style={{ margin: "10px auto", textAlign: "center" }}>
        <div>
          <label htmlFor="email">Email</label>
          <input
            type="email"
            id="email"
            value={user_email}
            readOnly
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
        <Bank/>
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
          <label htmlFor="password"> New Password</label>
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
            New Confirm Password
          </label>
          <input
            type="password"
            id="confirm-password"
            value={confirmPassword}
            onChange={onConfirmPasswordHandler}
            required
          />
        </div>
          <button onClick={onSubmit}>
            Submit
          </button>
          <button type="delete" onKeyDown={enterkey} onClick={onDelete}>
              Delete
          </button>
      </form>
    </div>
  );
};

export default Modify;
