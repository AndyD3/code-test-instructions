import { UseBaseMutationResult } from '@tanstack/react-query'
import { useMutation, useQueryClient } from '@tanstack/react-query'
import { AxiosResponse } from 'axios'
import axiosClient from 'axios'
import { ShortenedUrl } from '../types'

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
    },
  })
}