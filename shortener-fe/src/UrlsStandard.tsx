import { useReadUrls } from "./hooks/useReadShortenedUrls";
import { UrlsTable } from "./UrlsTable";

type Props = {
    deleteUrl: (arg0: string) => void
}  

export const UrlsStandard = ({ deleteUrl}: Props) => {

    const { data: shortenedUrls, isLoading, isError} = useReadUrls()

    return (
    <>
    
      {isError && <div style={{color: "red"}}>An error has occurred reading URLs...</div>}
      {isLoading && <div>Loading URLs...</div>}    
      
      {shortenedUrls instanceof Array && <UrlsTable shortenedUrls={shortenedUrls} deleteUrl={deleteUrl}/>}
    </>

    )
}