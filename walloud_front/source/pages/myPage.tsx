import { css } from "@emotion/react";
import { useRecoilValue } from "recoil";
import MobileContainer from "../layout/container/mobileContainer";
import PageContainer from "../layout/container/pageContainer";
import { userState } from "../recoils/user";
import UploadUserImageBox from "../component/box/uploadUserImageBox";
import { travelListState } from "../recoils/travel";
const MyPageStyle = css`
  width: 30%;
  align-items: center;
  & div {
    :hover {
      opacity: 70%;
    }

    & div {
      max-width: 40px;
      /* & img {
        max-width: 40px;
      } */
    }
  }
`;

function MyPage() {
  const User = useRecoilValue(userState);
  const TravelList = useRecoilValue(travelListState);
  console.log(User);
  return (
    <MobileContainer>
      <div id="my-page" css={MyPageStyle}>
        {UploadUserImageBox(User.id)}
        <h3>{User.name} </h3>
        <h4>
          {User.bank} {User.account}
        </h4>
        <h4>여행 참여 횟수: {TravelList.length}회</h4>
      </div>
    </MobileContainer>
  );
}

export default MyPage;
