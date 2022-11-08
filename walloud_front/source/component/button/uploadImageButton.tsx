import { ChangeEvent } from "react";

const UploadImageButton = (id: { id: number }) => {
  const onChangeInput = (e: any) => {
    var file: File = e.target.files[0];
    var reader: FileReader = new FileReader();
    reader.readAsDataURL(file);

    reader.onload = function () {
      var image = document.createElement("img");
      image.src = URL.createObjectURL(file);
      image.style.width = "100%";
      image.style.borderTopLeftRadius = "15px";
      image.style.borderTopRightRadius = "15px";
      document.getElementById(`${id.id}-image`).appendChild(image);
    };
    console.log(document.getElementById(`${id.id}-image`));
  };

  return (
    <div>
      <img
        alt="upload"
        src="source/assets/icon/upload.svg"
        onClick={() => document.getElementById(`${id.id}-upload`).click()}
      />
      <input
        id={`${id.id}-upload`}
        type="file"
        accept=".jpeg, .jpg, .png"
        style={{ display: "none" }}
        onChange={(e) => onChangeInput(e)}
      />
    </div>
  );
};

export default UploadImageButton;
