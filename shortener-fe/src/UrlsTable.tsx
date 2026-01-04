import { ShortenedUrl } from "./types";

type Props = {
    deleteUrl: (arg0: string) => void
    shortenedUrls?: ShortenedUrl[];
}  

export const UrlsTable = ({ deleteUrl, shortenedUrls }: Props) => {
    return (
        <>
        <h2>Stored URLs</h2>
        <table className="dataTable">
          <thead>
            <tr>
              <th>Alias</th>
              <th>Short URL</th>
              <th>Full URL</th>
              <th/>
            </tr>
          </thead>
          <tbody>
            <tr>
                <td>N/A</td>
                <td>N/A</td>
                <td>N/A</td>
                <td>
                  <button
                    aria-label="delete"
                    className="caution"
                    onClick={() => deleteUrl("non_existant_url")}
                  >
                  Delete (test non existant alias)
                  </button>
                </td>
             </tr>          
            {shortenedUrls?.length == 0 ? (
                <tr>
                  <td colSpan={3}>No Urls</td>
                </tr>
              ) : (
                shortenedUrls?.map((url) => ( 
                  <tr key={url.alias}>
                    <td>{url.alias}</td>
                    <td><a href={url.shortUrl} target="_blank">{url.shortUrl}</a></td>
                    <td>{url.fullUrl}</td>
                    <td>
                      <button
                        aria-label="delete"
                        className="caution"
                        onClick={() => deleteUrl(url.shortUrl!)}
                      >
                      Delete
                      </button>
                    </td>
                  </tr>
                  )
                )  
              )
            }
          </tbody>
        </table>    
    </>
    )
}