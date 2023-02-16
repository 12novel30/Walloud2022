import axios from "axios";
import UploadUserImageAPI from "../../api/uploadUserImageAPI";

const UploadUserImageBox = (userId: number) => {
  axios
    .get(`/api/${userId}/getUserImage`)
    .then((response) => {
      const div = document.getElementById(`${userId}-image`);
      // div.style.backgroundImage = `url(${response.data})`;
      // div.style.backgroundSize = "cover";
      const image = document.createElement("img");
      image.id = `${userId}-img`;
      image.style.width = "100%";
      image.style.height = "auto";
      if (response.data === "") {
        image.src =
          "https://walloud-bucket-ver2.s3.ap-northeast-2.amazonaws.com/test/ac28ab47-ad36-49ba-84ab-0398f3324ee9gang.jpg";
      } else {
        image.src = response.data;
      }

      div.appendChild(image);
    })
    .catch((error) => {
      console.log(error);
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      } else {
        alert("예기치 못한 오류가 발생했습니다");
      }
    });

  const onChangeInput = (e: any) => {
    var file: File = e.target.files[0];
    var reader: FileReader = new FileReader();

    reader.readAsDataURL(file);

    reader.onload = function () {
      const imageSrc = URL.createObjectURL(file);

      const div = document.getElementById(`${userId}-image`);
      div.removeChild(document.getElementById(`${userId}-img`));
      const newImage = document.createElement("img");
      newImage.src = imageSrc;
      newImage.id = `${userId}-img`;
      newImage.style.width = "100%";
      newImage.style.height = "auto";
      div.appendChild(newImage);

      var formData = new FormData();
      formData.append("file", file);
      UploadUserImageAPI(userId, formData);
    };
  };

  return (
    <div
      id={`${userId}-image`}
      style={{ backgroundColor: "black", width: "100%" }}
      onClick={(e) => document.getElementById(`${userId}-upload`).click()}
    >
      <input
        id={`${userId}-upload`}
        type="file"
        accept=".jpeg, .jpg, .png"
        onChange={(e) => onChangeInput(e)}
        style={{ display: "none" }}
      />
    </div>
  );
};

export default UploadUserImageBox;
