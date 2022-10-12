import { React, useState, useEffect } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import CreateTravel from "./CreateTravel";
import personPng from "../img/person.png";

const SelectTravel = () => {
  const user = useLocation().state.id;
  const [myTravel, setTravellist] = useState([]);
  const [try_del, setDelete] = useState(false);
  const [user_info, setUser_info] = useState({
    account: "",
    email: "",
    name: "",
  });
  const [checkAllButton, setCheckAllButton] = useState("전체 선택");
  const [checkedItems, setCheckedItems] = useState([]);
  const [checkedTravel, setCheckedTravel] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    getInfor();
  }, []);

  const getInfor = async () => {
    await axios
      .get(`/api/${user}`)
      .then((response) => {
        setTravellist(response.data.travelList);
        setUser_info({
          account: response.data.account,
          email: response.data.email,
          name: response.data.name,
        });
        console.log(response.data);
      })
      .catch((error) => {
        if (error.response.data.status === 500) {
          alert(error.response.data.message);
        }
      });
  };

  const Logout = () => {
    document.location.href = "/login";
  };

  const modify_info = () => {
    navigate(`/${user}/modify`, {state : {user_info : user_info, user : user}});
  }

  // Delete

  const try_Delete = () => {
    if (try_del) {
      if (checkedItems.length !== 0) {
        if (
          window.confirm(
            checkedItems + " 이 선택되었습니다.\n 삭제하시겠습니까?"
          )
        ) {
          checkedItems.map((travel_id) => {
            axios
              .delete(`/api/${user}/${travel_id}/delete`)
              .then(() => {
                alert("삭제되었습니다.");
              })
              .catch((error) => {
                if (error.response.data.status === 500) {
                  alert(error.response.data.message);
                }
              });
          });
          window.location.reload();
        } else {
          setCheckedItems([]);
          setCheckedTravel([]);
          console.log("unchecked all", checkedItems);
          alert("취소되었습니다.");
        }
      }
    }
    setDelete(!try_del);
  };

  const checkHandler = (checked, elem) => {
    if (checked) {
      setCheckedItems((prev) => [...prev, elem.travelId]);
      setCheckedTravel((prev) => [...prev, elem.name]);
      console.log(elem, "push", checkedItems);
    } else {
      setCheckedItems(checkedItems.filter((e) => e !== elem.travelId));
      setCheckedTravel(checkedTravel.filter((e) => e !== elem.name));
      console.log(elem, "pop", checkedItems);
    }
  };

  const handleAllCheck = (checked) => {
    if (checked) {
      setCheckAllButton("전체 선택");
      const idArray = [];
      const travelArray = [];
      myTravel.forEach((e) => {
        idArray.push(e.travelId);
        travelArray.push(e.name);
      });
      setCheckedItems(idArray);
      setCheckedTravel(travelArray);
      console.log("checked all", checkedItems);
    } else {
      setCheckAllButton("전체 선택");
      setCheckedItems([]);
      setCheckedTravel([]);
      console.log("unchecked all", checkedItems);
    }
  };

  return (
    <div>
      <h1>Divide by N</h1>
      <h2>My Travel List</h2>
      <div style={{ display: "flex" }}>
        <div style={{ width: "50%", borderRight: "3px solid black" }}>
          <img src={personPng} alt="me" />
          <h3>{user_info.name}</h3>
          <h4>Email: {user_info.email}</h4>
          <h4> Account : {user_info.account}</h4>
          <div style={{ display: "flex" }}>
            <button onClick={Logout}>Log Out</button>
            <button onClick={modify_info}>Modify</button>
          </div>
        </div>
        <div style={{ width: "50%" }}>
          <h3>Existing Travels</h3>
          {myTravel.length !== 0 ? (
            <div>
              {!try_del ? (
                <div>
                  {myTravel.map((travel, idx) => (
                    <Link
                      key={idx}
                      to={`/${user}/${travel.travelId}/${travel.name}`}
                      state={{
                        created: false,
                      }}
                    >
                      <h4
                        className="link-text"
                        style={{
                          display: "block",
                          margin: "auto",
                          textAlign: "center",
                        }}
                      >
                        {travel.name}
                      </h4>
                      <br />
                    </Link>
                  ))}
                </div>
              ) : (
                <div className="contStyle">
                  <div style={{ alignItems: "center" }}>
                    {myTravel.map((travel) => (
                      <div
                        style={{
                          display: "inline-block",
                          minWidth: "33%",
                          alignItems: "center",
                          marginBottom: "3%",
                        }}
                      >
                        <input
                          id={travel.travelId}
                          style={{ display: "inline-block", margin: "0" }}
                          className="checkbox"
                          type="checkbox"
                          value={travel.travelId}
                          onChange={(e) =>
                            checkHandler(e.target.checked, travel)
                          }
                          checked={
                            checkedItems.includes(travel.travelId)
                              ? true
                              : false
                          }
                        />
                        <label
                          htmlFor={travel.travelId}
                          className="checkbox-text"
                        >
                          {travel.name}
                        </label>
                      </div>
                    ))}
                  </div>
                  <div
                    style={{
                      display: "inline-block",
                      marginBottom: "3%",
                    }}
                  >
                    <input
                      id="checkAll"
                      type="checkbox"
                      className="checkbox"
                      onChange={(e) => handleAllCheck(e.target.checked)}
                      checked={
                        checkedItems.length === myTravel.length ? true : false
                      }
                    ></input>
                    <label htmlFor="checkAll" className="checkbox-text">
                      {checkAllButton}
                    </label>
                  </div>
                </div>
              )}
              <button onClick={try_Delete}>Delete</button>
            </div>
          ) : (
            <div>Create</div>
          )}

          <CreateTravel user={user} myTravel={myTravel} />
        </div>
      </div>
    </div>
  );
};

export default SelectTravel;
