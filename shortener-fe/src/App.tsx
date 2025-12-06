import './App.css'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { useReadUrls } from './hooks/useReadShortenedUrls'

const queryClient = new QueryClient()

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <URLManager />
    </QueryClientProvider>
  )
}

function URLManager() {
  const { data: shortenedUrls, isLoading, isError} = useReadUrls()

  //TODO remove
  console.log(shortenedUrls);
  console.log(isError);

  return (
    <>
      <h1>URL shortener tester</h1>
      {isError && <div style={{color: "red"}}>An error has occurred...</div>}

      {isLoading && <div>Loading...</div>}

      retrieved URLs:
      <table>
        <tbody>
          {shortenedUrls?.length == 0 ? (
              <tr>
                <td colSpan={3}>No Urls</td>
              </tr>
            ) : (
              shortenedUrls?.map((url) => ( 
                <tr key={url.alias}>
                  <td>{url.alias}</td>
                  <td>{url.shortUrl}</td>
                  <td>{url.fullUrl}</td>
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
