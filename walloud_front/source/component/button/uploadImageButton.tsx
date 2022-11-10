const UploadImageButton = (id: { id: number }) => {
  const onChangeInput = (e: any) => {
    var file: File = e.target.files[0];
    var reader: FileReader = new FileReader();

    reader.readAsDataURL(file);

    reader.onload = function () {
      const imageSrc = URL.createObjectURL(file);
      document.getElementById(
        `${id.id}-image`
      ).style.backgroundImage = `url(${imageSrc})`;
      document.getElementById(`${id.id}-image`).innerText = "";
    };
    console.log(document.getElementById(`${id.id}-image`));
  };

  return (
    <div style={{ display: "none" }}>
      <input
        id={`${id.id}-upload`}
        type="file"
        accept=".jpeg, .jpg, .png"
        onChange={(e) => onChangeInput(e)}
      />
    </div>
  );
};

export default UploadImageButton;
