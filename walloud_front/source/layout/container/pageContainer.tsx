import React from "react";
import FootBar from "../../component/footer/footBar";
import NavBar from "../../component/navigator/navBar";

function PageContainer(props: any){
  return (
    <>
      <NavBar />
      {props.children}
      <FootBar />
    </>
  )
}

export default PageContainer;