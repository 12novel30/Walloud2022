import { css } from '@emotion/react'
import PageContainer from "../../layout/container/pageContainer";
import JoinSection from './joinSection';
import VisualSection from "./visualSection";

function IntroPage() {
  return (
      <div css = {{
        '&>div': {
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          width: '100vw',
          height: '100vh'
        },
      }}>
        <VisualSection />
        <JoinSection />
      </div>
  );
}

export default IntroPage;