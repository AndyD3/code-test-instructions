import { UseBaseMutationResult } from '@tanstack/react-query'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { AxiosError, AxiosResponse } from 'axios'
import axiosClient from 'axios'
import { ShortenedUrl } from '../types'
import toast from 'react-hot-toast'

const deleteUrl = async (shortUrl: string): Promise<AxiosResponse<ShortenedUrl[]>> => {
  return await axiosClient.delete(`${shortUrl}`)
}

export const useDeleteUrl = (): UseBaseMutationResult<
  AxiosResponse<ShortenedUrl[], unknown>,
  unknown,
  string,
  unknown
> => {
  const queryClient = useQueryClient()
  return useMutation({
    mutationFn: (shortUrl: string) => deleteUrl(shortUrl),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['tasks'] })      
      toast.success(`Short URL successfully deleted`)
    }
    ,
    onError: (error: AxiosError) => toast.error(`Error deleting Short URL. Error message was:\n ${error?.response?.data ? error?.response?.data : "<no message body>"}`)    
  })
}